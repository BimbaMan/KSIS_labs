using System.Net;
using System.Net.Sockets;
using System.Text;

namespace Chat;

public class User
{
    public string name;
    public readonly IPAddress ip;
    private static int port = 8005;
    private TcpClient client;
    private NetworkStream stream;
    
    public User(string name, IPEndPoint IPEP)
    {
        this.name = name;
        ip = IPEP.Address;
    }

    public User(TcpClient tcpclient, int userport)
    {
        this.client = tcpclient;
        port = userport;
        ip = ((IPEndPoint)this.client.Client.RemoteEndPoint).Address;
        stream = this.client.GetStream();
    }

    public void EstablishConnection()
    {
        client = new TcpClient();
        IPEndPoint IPEP = new IPEndPoint(ip, port);
        client.Connect(IPEP);
        stream = client.GetStream();
    }

    public void SendMessage(string message)
    {
        byte[] data = Encoding.UTF8.GetBytes(message);
        stream.Write(data, 0, data.Length);
    }

    public string ReceiveMessage()
    {
        byte[] data = new byte[1024];
        StringBuilder message = new StringBuilder();
        do
        {
            try
            {
                int size = stream.Read(data, 0, data.Length);
                message.Append(Encoding.UTF8.GetString(data, 0, size));
            }
            catch
            {
                return "1";
            }
        }
        while (stream.DataAvailable);

        string messageString = message.ToString();
        return messageString;
    }

    public void CloseConnection()
    {
        stream.Close();
        client.Close();
    }
}