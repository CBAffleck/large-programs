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
        if channelName in channels.keys():
            error_msg = utils.SERVER_CHANNEL_EXISTS.format(channelName)
            self.sendToSelf("\r" + error_msg, sock)
        else:
            for value in channels.itervalues():
                if clientUserName in value:
                    leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(clientUserName)
                    self.sendToOthers("\r" + leave_msg, self.socket, sock, clientUserName)
                    value.remove(clientUserName)
            channels[channelName] = [clientUserName]
            self.sendToSelf("\r" + utils.SERVER_CHANNEL_JOINED.format(channelName), sock)
            sys.stdout.flush()

    ## Used to let the client join a pre-existing channel
    def joinChannel(self, channelName, clientUserName, sock, password):
        # Check if channel already exists
        current_channel = False
        if channelName in channels.keys():
            for key, value in channels.iteritems():
                # Removes client from channel they were previously in
                if clientUserName in value:
                    # Check if the channel being joined is the channel the client is already in
                    if key == channelName:
                        current_channel = True
                        break
                    else:
                        leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(clientUserName)
                        self.sendToOthers("\r" + leave_msg, self.socket, sock, clientUserName)
                        value.remove(clientUserName)
            access = True
            if channelName in protected_channels:
                # Make sure channel is not current channel, since in that case we don't need to rejoin
                if password != protected_channels[channelName] and not current_channel:
                    error_msg = utils.SERVER_CHANNEL_PASS_FALSE.format(channelName) # No channel with that name exists
                    self.sendToSelf("\r" + error_msg, sock)
                    access = False
            # Tell client they're trying to join the channel they're currently in
            if current_channel:
                self.sendToSelf("\r" + utils.SERVER_CHANNEL_CURRENT.format(channelName), sock)
                sys.stdout.flush()
            # If we have access to the channel and it's a new channel, perform these actions
            elif access == True:
                channels[channelName].append(clientUserName)
                join_msg = utils.SERVER_CLIENT_JOINED_CHANNEL.format(clientUserName)
                self.sendToOthers("\r" + join_msg, self.socket, sock, clientUserName) # Announces to the new channel that this client has joined
                self.sendToSelf("\r" + utils.SERVER_CHANNEL_JOINED.format(channelName), sock)
                sys.stdout.flush()
        else:
            error_msg = utils.SERVER_NO_CHANNEL_EXISTS.format(channelName) # No channel with that name exists
            self.sendToSelf("\r" + error_msg, sock)

    def sendToOthers(self, message, server_sock, sock, username):
        # Catches exception where the user is not sending the message, and the server is sending a message
        # This includes the server letting others in a channel know that a new client has joined the channel
        if " " in message: # A space will always be in the message unless the client typed nothing, so they were all stripped off
            # Checks if the client is already in a channel, and if not sends the client the "not in channel" error message 
            in_channel = False
            for value in channels.itervalues():
                if username in value:
                    in_channel = True
                    for socket in clients.keys():
                        try:
                            # Don't send message to server or self, but to all other clients in channel
                            if socket != server_sock and socket != sock and clients[socket] in value:
                                try:
                                    socket.send(message)
                                except:
                                    socket.close()
                                    clients.pop(socket)
                        except KeyError: # passes over KeyError occurring when the name is an integer
                            pass
            if not in_channel:
                error_msg = utils.SERVER_CLIENT_NOT_IN_CHANNEL
                self.sendToSelf("\r" + error_msg, sock)
        else:
            pass

    def sendWhisper(self, message, sock, username):
        exists = False
        for socket, name in clients.iteritems():
            if name == username:
                exists = True
                socket.send(message)
        if not exists:
            error_msg = utils.SERVER_CLIENT_DOESNT_EXIST.format(username)
            self.sendToSelf("\r" + error_msg, sock)

    def changeUserName(self, newName, sock):
        clients[sock] = newName
    
    ## Used by the server to send a message to a client trying to do something that isn't supported or that throws an error
    def sendToSelf(self, message, sock):
        for socket in clients.keys():
            if socket == sock:
                try:
                    socket.send(message)
                except:
                    socket.close()
                    clients.pop(socket, 'None')

    def start(self):
        clients[self.socket] = "server"
        while True:
            ready_to_read,ready_to_write,in_error = select.select(clients.keys(),[],[],0)
            for sock in ready_to_read:
                if sock == self.socket:
                    (new_socket, address) = self.socket.accept()
                    clients[new_socket] = address[1]
                else:
                    msg = sock.recv(1024)
                    if msg:
                        tmp = msg
                        while tmp < utils.MESSAGE_LENGTH:
                            tmp = new_socket.recv(1024)
                            msg += tmp
                        msg = msg.rstrip()
                        clientUserName = ""
                        if " " in msg:
                            msg_start = msg.index(" ") + 1
                            clientUserName = msg[1:msg_start - 2]
                        else:
                            msg_start = msg.index("]")
                            clientUserName = msg[1:msg_start]
                        if clientUserName in clients.values():
                            self.sendToSelf("\r" + utils.SERVER_USERNAME_TAKEN.format(clientUserName), sock)
                        destination = sock.getpeername()
                        # Replace the port in the namelist with the client's username
                        if destination[1] in clients.values():
                            clients[sock] = clientUserName
                        # Deal with different server commands
                        if msg[msg_start] == "/":
                            # List all channels that are currently available to join
                            if msg[msg_start:msg_start + 5] == "/list":
                                client_wipe = utils.CLIENT_WIPE_ME
                                lst = client_wipe + "\r"
                                for i in channels.keys():
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
                                if length > 2:
                                    self.sendToSelf("\r" + utils.SERVER_CREATE_MANY_ARGUMENTS, sock)
                                elif length == 2:
                                    channelName = channel_list[0]
                                    if channelName not in channels.keys():
                                        protected_channels[channelName] = channel_list[1]
                                    self.createChannel(channelName, clientUserName, sock)
                                elif length == 0:
                                    self.sendToSelf("\r" + utils.SERVER_CREATE_REQUIRES_ARGUMENT, sock)
                                else:
                                    channelName = channel_list[0]
                                    self.createChannel(channelName, clientUserName, sock)

                            # Attempt to join channel
                            elif msg[msg_start:msg_start + 5] == "/join":
                                channel_list = msg[msg_start + 6:].split()
                                length = len(channel_list)
                                if length > 2:
                                    self.sendToSelf("\r" + utils.SERVER_JOIN_MANY_ARGUMENTS, sock)
                                elif length == 2:
                                    channelName = channel_list[0]
                                    password = channel_list[1]
                                    self.joinChannel(channelName, clientUserName, sock, password)
                                elif length == 0:
                                    self.sendToSelf("\r" + utils.SERVER_JOIN_REQUIRES_ARGUMENT, sock)
                                else:
                                    channelName = channel_list[0]
                                    self.joinChannel(channelName, clientUserName, sock, 'None')

                            elif msg[msg_start:msg_start + 8] == "/whisper":
                                msg_list = msg.split()
                                length = len(msg_list)
                                if length < 4:
                                    self.sendToSelf("\r" + utils.SERVER_WHISPER_REQUIRES_NAME, sock)
                                else:
                                    username = msg_list[2]
                                    command = msg_list[1]
                                    parsed_msg = ' '.join(msg_list[3:])
                                    restructured_msg = "\r" + msg[:msg_start] + command[1:] + ": " + parsed_msg
                                    self.sendWhisper(restructured_msg, sock, username)
                                    sys.stdout.flush()

                            elif msg[msg_start:msg_start + 11] == "/changeName":
                                msg_list = msg[msg_start + 12:].split()
                                length = len(msg_list)
                                if length > 1:
                                    self.sendToSelf("\r" + utils.SERVER_CHANGENAME_MANY_ARGUMENTS, sock)
                                elif msg_list[0] == clientUserName:
                                    self.sendToSelf("\r" + utils.SERVER_SAME_USERNAME, sock)
                                else:
                                    self.changeUserName(msg_list[0], sock)

                            # Notify client that whatever control they're trying to use doesn't exist
                            else:
                                control = msg[msg_start:]
                                error_msg = utils.SERVER_INVALID_CONTROL_MESSAGE.format(control)
                                self.sendToSelf("\r" + error_msg, sock)
                        else:
                            self.sendToOthers("\r" + msg, self.socket, sock, clientUserName)
                            sys.stdout.flush()
                    else:
                        if sock in clients:
                            leaving = clients.pop(sock, 'None')
                            leave_msg = utils.SERVER_CLIENT_LEFT_CHANNEL.format(leaving)
                            self.sendToOthers("\r" + leave_msg, self.socket, sock, leaving)
                            for value in channels.itervalues():
                                if leaving in value:
                                    value.remove(leaving)


if len(args) != 2:
    print "Please supply a port."
    sys.exit()
server = BasicServer(args[1])
protected_channels = {} # Format = {'channel' : password}
channels = {} # Format = {'channel' : ['name']}
clients = {} # Format = {socket : 'name'}
len_none = len(utils.CLIENT_WIPE_ME + "\r")
server.start()