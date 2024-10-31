using System;
using System.IO;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using InTheHand.Net.Bluetooth;
using InTheHand.Net.Sockets;

namespace ClipboardSync
{
    public class ClipboardReceiver
    {
        private const string BluetoothServiceUuid = "00001101-0000-1000-8000-00805F9B34FB";
        private BluetoothClient bluetoothClient;
        private NetworkStream networkStream;

        public ClipboardReceiver()
        {
            bluetoothClient = new BluetoothClient();
        }

        public void Start()
        {
            var bluetoothListener = new BluetoothListener(BluetoothServiceUuid);
            bluetoothListener.Start();

            while (true)
            {
                try
                {
                    bluetoothClient = bluetoothListener.AcceptBluetoothClient();
                    networkStream = bluetoothClient.GetStream();

                    var buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = networkStream.Read(buffer, 0, buffer.Length)) > 0)
                    {
                        var receivedData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
                        UpdateClipboard(receivedData);
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Error: " + ex.Message);
                }
                finally
                {
                    networkStream?.Close();
                    bluetoothClient?.Close();
                }
            }
        }

        private void UpdateClipboard(string data)
        {
            Thread thread = new Thread(() => Clipboard.SetText(data));
            thread.SetApartmentState(ApartmentState.STA);
            thread.Start();
            thread.Join();
        }

        [STAThread]
        static void Main()
        {
            var receiver = new ClipboardReceiver();
            receiver.Start();
        }
    }
}
