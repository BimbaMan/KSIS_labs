using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Chat
{
    static class Chat
    {
        private static string? username = "";
        private static IPAddress UserIP;
        private static readonly IPAddress broadcastIP = IPAddress.Broadcast;
        private const int UdpPort = 8004;
        private const int TcpPort = 8005;
        private static bool isConnected = true;
        private static readonly Settings settings = new Settings();
        private static Task receiveThreadTCP;
        private static Task receiveThreadUDP;

        public static void Main(string[] args)
        {
            Console.WriteLine("Welcome to the chat!");
            InitializeUser();
            Console.Clear();
            SendMessageUdp("0" + username);

            receiveThreadUDP = new Task(ReceiveMessageUdp);
            receiveThreadUDP.Start();
            Console.WriteLine(DateTime.Now.ToString("HH:mm:ss") + ":" + username + " " + UserIP + " " + "entered the chat");
            receiveThreadTCP = new Task(ReceiveMessageTcp);
            receiveThreadTCP.Start();

            string inputmessage = "";

            while (true)
            {
                inputmessage = Console.ReadLine();
                if (inputmessage == ":exit")
                {
                    isConnected = false;
                    SendMessageToAll("1");
                    if (settings.UserList.Count != 0)
                    {
                        foreach (var user in settings.UserList)
                        {
                            user.CloseConnection();

                        }
                    }
                    Console.WriteLine("Good Bye!");
                    return;
                }
                else
                {
                    SendMessageToAll("2" + inputmessage);
                }
            }
        }
        
        private static void InitializeUser()
        {
            while (true)
            {
                Console.WriteLine("Enter your username: ");
                username = Console.ReadLine()?.Trim();
                if (username.Length == 0)
                {
                    Console.WriteLine("Username cannot be empty");
                }
                else
                { break; }
            }

            while (true)
            {
                Console.WriteLine("Enter your IP: ");
                string ip = Console.ReadLine()?.Trim();
                if (IPAddress.TryParse(ip, out UserIP))
                { return; }
                else
                { Console.WriteLine("Invalid IP"); }
            }
        }

        private static void SendMessageUdp(string message)
        {
            UdpClient client = new UdpClient( new IPEndPoint(UserIP, UdpPort));
            try
            {
                byte[] data = Encoding.UTF8.GetBytes(message);
                client.Send(data, data.Length, broadcastIP.ToString(), UdpPort);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            finally
            {
                client.Close();
            }
        }

        private static void ReceiveMessageUdp()
        {
            IPEndPoint remote = new IPEndPoint(IPAddress.Any, UdpPort);;
            UdpClient reciever = new UdpClient(new IPEndPoint(UserIP, UdpPort));
            while (isConnected)
            {
                byte[] data = reciever.Receive(ref remote);
                string message = Encoding.UTF8.GetString(data);
                string forPrint = settings.WelcomeUser(message);
                
                User newUser = new User(message.Substring(1), remote);
                newUser.EstablishConnection();
                settings.UserList.Add(newUser);
                newUser.SendMessage("0" + username);
                
                Console.WriteLine(DateTime.Now.ToString("HH:mm:ss") + ":" + newUser.name + " " + remote.Address + " " + forPrint + "entered the chat");
                Task.Factory.StartNew(() => ListenClient(newUser));
            }
            reciever.Close();
            reciever.Dispose();
        }

        private static void ListenClient(User user)
        {
            while (isConnected)
            {
                string message = user.ReceiveMessage();
                switch (message[0])
                {
                    case '0':
                    {
                        user.name = message.Substring(1);
                        settings.UserList.Add(user);
                        break;
                    }
                    case '1':
                    {
                        Console.WriteLine(DateTime.Now.ToString("HH:mm:ss") + ":" + user.name + " " + user.ip + "left the chat");
                        settings.UserList.Remove(user);
                        return;
                    }
                    case '2':
                    {
                        Console.WriteLine(DateTime.Now.ToString("HH:mm:ss") + ":" + user.name + " " + user.ip + ": " + message.Substring(1));
                        break;
                    }
                }
            }

            
        }
        
        private static void ReceiveMessageTcp()
        {
            TcpListener listener = new TcpListener(UserIP, TcpPort);
            listener.Start();
            while (true)
            {
                TcpClient newclient = listener.AcceptTcpClient();
                User newUser = new User(newclient, TcpPort);
                
                Task.Factory.StartNew(() => ListenClient(newUser));
            }
        }
        
        private static void SendMessageToAll(string message)
        {
            foreach (var user in settings.UserList)
            {
                try
                {
                    user.SendMessage(message);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    throw;
                }
            }
            if (message[0] == '2')
            {
                Console.WriteLine(DateTime.Now.ToString("HH:mm:ss") + ": You:" + message.Substring(1));
            }
            
        }

    }
}