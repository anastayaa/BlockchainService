
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pubsub.dp.Ledger;
import sys.user.Node;

public class TestClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 1234);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Node node = Ledger.getInstance().addNode();
		System.err.println(node.getAddress());
		oos.writeObject(node);
		int status = ois.readInt();
		System.out.println(status);
		socket.close();
	}
}


