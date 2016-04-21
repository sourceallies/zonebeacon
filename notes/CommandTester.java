import java.util.Scanner;

// This is a simple command line java program that can send commands to the 
// CentraLite briefcase for testing.
//
// To execute this, run:
// $ javac CommandTester.java
// $ java CommandTester
public class CommandTester {

    private static final String IP = "192.168.1.150";
    private static final int PORT = 11000;

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("What do you want to send: ");
            String message = reader.nextLine();

            try {
                Socket socket = new Socket(IP, PORT);

                InputStream is = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                PrintWriter w = new PrintWriter(out, true);
                w.print(message + "\r\n");
                w.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                int value = 0;

                while (br.ready() && (value = br.read()) != -1) {
                    char c = (char) value;
                    System.out.print(c);
                }

                socket.close();

                System.out.println("");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error: " + e.getMessage());
            }
        }
    }
}