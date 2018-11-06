import socket
import sys
import utils
import select

args = sys.argv

class BasicServer(object):
    
    def __init__(self, port):
        self.socket = socket.socket()
        self.socket.bind(("", int(port)))
        self.socket.listen(5)

    def createChannel(self, channelName, clientUserName, sock):
        if channelName in available_channels:
            error_msg = utils.SERVER_CHANNEL_EXISTS.format(channelName)
            self.sendToSelf("\r" + error_msg, sock)
        else:
            available_channels.append(channelName)
            if clientUserName in channels:
                leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(clientUserName)
                self.sendToOthers("\r" + leave_msg, self.socket, sock)
            channels[clientUserName] = channelName
            sys.stdout.flush()

    ## Used to let the client join a pre-existing channel
    def joinChannel(self, channelName, clientUserName, sock):
        if channelName in available_channels:
            if clientUserName in channels:
                leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(clientUserName)
                self.sendToOthers("\r" + leave_msg, self.socket, sock) # Lets other clients know that this client is leaving
                del channels[clientUserName]
            channels[clientUserName] = channelName
            join_msg = utils.SERVER_CLIENT_JOINED_CHANNEL.format(clientUserName)
            self.sendToOthers("\r" + join_msg, self.socket, sock) # Announces to the new channel that this client has joined
            sys.stdout.flush()
        else:
            error_msg = utils.SERVER_NO_CHANNEL_EXISTS.format(channelName) # No channel with that name exists
            self.sendToSelf("\r" + error_msg, sock)

    def sendToOthers(self, message, server_sock, sock):
        # Catches exception where the user is not sending the message, and the server is sending a message
        # This includes the server letting others in a channel know that a new client has joined the channel
        if " " in message: # A space will always be in the message unless the client typed nothing, so they were all stripped off
            try:
                name_end = message.index("]")
                clientUserName = message[2:name_end]
            except ValueError:
                name_end = message.index(" ")
                clientUserName = message[1:name_end]
            # Checks if the client is already in a channel, and if not sends the client the "not in channel" error message 
            if clientUserName in channels:
                # Looks through SOCKET_LIST and name_list simultaneously to match the socket to a client name
                for socket, name in zip(SOCKET_LIST, name_list): 
                    try:
                        if socket != server_sock and socket != sock and channels[name] == channels[clientUserName]:
                            try:
                                socket.send(message)
                                print message
                            except:
                                socket.close()
                                if socket in SOCKET_LIST:
                                    SOCKET_LIST.remove(socket)
                    except KeyError: # passes over KeyError occurring when the name is an integer in name_list
                        pass
            else:
                error_msg = utils.SERVER_CLIENT_NOT_IN_CHANNEL
                self.sendToSelf("\r" + error_msg, sock)
        else:
            pass
    
    ## Used by the server to send a message to a client trying to do something that isn't supported or that throws an error
    def sendToSelf(self, message, sock):
        for socket in SOCKET_LIST:
            if socket == sock:
                try:
                    socket.send(message)
                except:
                    socket.close()
                    if socket in SOCKET_LIST:
                        SOCKET_LIST.remove(socket)

    def start(self):
        SOCKET_LIST.append(self.socket)
        name_list.append("server")
        while True:
            ready_to_read,ready_to_write,in_error = select.select(SOCKET_LIST,[],[],0)
            for sock in ready_to_read:
                if sock == self.socket:
                    (new_socket, address) = self.socket.accept()
                    SOCKET_LIST.append(new_socket)
                    name_list.append(address[1])
                else:
                    msg = sock.recv(1024)
                    if msg:
                        tmp = msg
                        while tmp < utils.MESSAGE_LENGTH:
                            tmp = new_socket.recv(1024)
                            msg += tmp
                        msg = msg.rstrip()
                        if " " in msg:
                            msg_start = msg.index(" ") + 1
                        else:
                            msg_start = msg.index("]")
                        clientUserName = msg[1:msg_start - 2]
                        destination = sock.getpeername()
                        # Replace the port in the namelist with the client's username
                        if destination[1] in name_list:
                            i = name_list.index(destination[1])
                            name_list[i] = clientUserName
                        # Deal with different server commands
                        if msg[msg_start] == "/":
                            # List all channels that are currently available to join
                            if msg[msg_start:msg_start + 5] == "/list":
                                client_wipe = utils.CLIENT_WIPE_ME
                                lst = client_wipe + "\r"
                                for i in available_channels:
                                    lst += i + "\n"
                                if len(lst) > len_none:
                                    self.sendToSelf(lst[:-1], sock)
                                    sys.stdout.flush()
                                else:
                                    sys.stdout.flush()
                                    pass

                            # Try to create a channel with the specified channel name
                            elif msg[msg_start:msg_start + 7] == "/create":
                                channel_list = msg[msg_start + 8:].split()
                                length = len(channel_list)
                                if length > 1:
                                    self.sendToSelf("\r" + utils.SERVER_CREATE_MANY_ARGUMENTS, sock)
                                elif length == 0:
                                    self.sendToSelf("\r" + utils.SERVER_CREATE_REQUIRES_ARGUMENT, sock)
                                else:
                                    channelName = channel_list[0]
                                    self.createChannel(channelName, clientUserName, sock)

                            # Attempt to join channel
                            elif msg[msg_start:msg_start + 5] == "/join":
                                channel_list = msg[msg_start + 6:].split()
                                length = len(channel_list)
                                if length > 1:
                                    self.sendToSelf("\r" + utils.SERVER_JOIN_MANY_ARGUMENTS, sock)
                                elif length == 0:
                                    self.sendToSelf("\r" + utils.SERVER_JOIN_REQUIRES_ARGUMENT, sock)
                                else:
                                    channelName = channel_list[0]
                                    self.joinChannel(channelName, clientUserName, sock)

                            # Notify client that whatever control they're trying to use doesn't exist
                            else:
                                control = msg[msg_start:]
                                error_msg = utils.SERVER_INVALID_CONTROL_MESSAGE.format(control)
                                self.sendToSelf("\r" + error_msg, sock)
                        else:
                            self.sendToOthers("\r" + msg, self.socket, sock)
                            sys.stdout.flush()
                    else:
                        if sock in SOCKET_LIST:
                            SOCKET_LIST.remove(sock)
                            leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(clientUserName)
                            self.sendToOthers("\r" + leave_msg, self.socket, sock)
                            del channels[clientUserName]
    

if len(args) != 2:
    print "Please supply a port."
    sys.exit()
server = BasicServer(args[1])
name_list = []
available_channels = []
channels = {}
SOCKET_LIST = []
len_none = len(utils.CLIENT_WIPE_ME + "\r")
server.start()