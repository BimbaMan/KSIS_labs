using System.Net;


namespace Chat;

public class Settings
{
    public List<User> UserList = new List<User>();

    public string WelcomeUser(string mesg)
    {
        if ((mesg[0].Equals("0")) && (mesg.Length >= 2))
        {
            string name = mesg.Substring(1);
            return "$(name) is connected";
        }
        else
        {
            return "";
        }   
    } 
}