import socket
import sys
import utils
import select

class BasicClient(object):

	def __init__(self, name, address, port):
		self.name = name
		self.address = address
		self.port = int(port)
		self.socket = socket.socket()

	def connect(self):
		self.socket.connect((self.address, self.port))
		sys.stdout.write(utils.SERVER_WELCOME)
		sys.stdout.flush()
		sys.stdout.write("[Me] ")
		sys.stdout.flush()
		initialize_msg = "[" + self.name + "] "
		self.socket.send(initialize_msg)
		while True:
			socket_list = [sys.stdin, self.socket]
			ready_to_read, ready_to_write, in_error = select.select(socket_list , [], [])
			for sock in ready_to_read:
				if sock == self.socket:
					msg_in = sock.recv(1024)
					if not msg_in:
						error_msg = utils.CLIENT_SERVER_DISCONNECTED.format(client.address, client.port)
						raise RuntimeError(error_msg)
					else:
						# if msg_in[0] == "/" and "name has successfully been changed" in msg_in:
						# 	msg_in_list = msg_in.split()
						# 	new_name = msg_in_list[-1][:-1]
						# 	print new_name
						# 	self.name = new_name
						print msg_in
						sys.stdout.write("[Me] ")
						sys.stdout.flush()
				else:
					msg = "[" + self.name + "] " + raw_input(utils.CLIENT_MESSAGE_PREFIX)
					self.socket.send(msg)
			sys.stdout.flush()

	def send(self, message):
		totalsent = 0
		while len(message) < utils.MESSAGE_LENGTH:
			message += " "
		while totalsent < utils.MESSAGE_LENGTH:
			sent = self.socket.send(message[totalsent:])
			if sent == 0:
				error_msg = utils.CLIENT_SERVER_DISCONNECTED.format(self.address, self.port)
				raise RuntimeError(error_msg)
			totalsent = totalsent + sent

args = sys.argv
if len(args) != 4:
	print "Please supply a server address, name, and port."
	sys.exit()
client = BasicClient(args[1], args[2], args[3])
try:
	client.connect()
except:
	error_msg = utils.CLIENT_CANNOT_CONNECT.format(client.address, client.port)
	raise RuntimeError(error_msg)