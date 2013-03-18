
//How to allow client to send input / output streams to server if its the server itself
//Client :-     INPUT 1(Keep) :CONVERT: ---->output1(SEND) :- server
//Client :-    output2(KEPT) :CONVERTED FROM: ---->(SENT)Input2 :- server

//try {
//						
//						PipedInputStream is = new PipedInputStream(8000);
//						PipedOutputStream outputStream = new PipedOutputStream(is);
//						
//					     OutputStream buffers = new BufferedOutputStream( outputStream );
//					     final ObjectOutput test = new ObjectOutputStream( buffers );
//					
//					     test.flush();
//					     
//					     final ObjectInputStream input = new ObjectInputStream( is );
//					     
//					     test.writeObject(quarks);
//							test.flush();
//					     
//							ArrayList<String> quarks2 = (ArrayList<String>) input.readObject();
//							
//							int testz = 0;
//							
//				
//					     
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ClassNotFoundException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				        
// 
//					
//	}


//package server;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class StreamConverters {
//	
//	public class InputStreamToOuputStream implements Runnable
//	{
//
//		private InputStream inStream;
//		private OutputStream outStream;
//		
//		public InputStreamToOuputStream(InputStream stream)
//		{
//			this.inStream = stream;		
//			this.outStream = new ByteArrayOutputStream();
//			
//		}
//		
//		public OutputStream getOutStream()
//		{
//			return outStream;
//		}
//		
//		@Override
//		public void run() {
//			
//			byte buffer[] = new byte[256];
//			
//			while(true)
//			{
//				try {
//					int count = this.inStream.read(buffer);
//					outStream.write(buffer, 0, count);
//				} catch (IOException e) {
//					try {
//						this.inStream.close();
//						this.outStream.close();
//					} catch (IOException e1) {
//						break;
//					}
//					break;
//				}
//			}
//		}
//		
//	}
//}
