package kr.javatpc;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.*;
public class MqttClass implements MqttCallback{
	
    private MqttClient client = null;
    public MqttClass(){
        new Thread(task1).start();
    }
    private ReceiveEventListner listener = null;

    Runnable task1 = new Runnable(){
        @Override
        public void run() {
            try {
            	String clientId = UUID.randomUUID().toString();
                //new MqttClient()
                client = new MqttClient("tcp://172.30.1.15:1883", clientId); 
                MqttConnectOptions connopt = new MqttConnectOptions();
                connopt.setCleanSession(true);
                client.connect(connopt);
                client.setCallback(MqttClass.this);
                client.subscribe("dht11");               

                new IoTFrame(MqttClass.this);

            } catch (MqttException e) {
                System.out.println("ERR0"+e.getStackTrace());
            }
        }
    };

    public void sendMessage(String payload){
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes()); 
        try {
            if(client.isConnected()){
                client.publish("led", message);
            }
        } catch (MqttException e) {
            System.out.println("error1-"+e.getStackTrace());//+e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable arg0) {
        try {
            System.out.println("disconect");
            client.close();
        } catch (MqttException e) {
            System.out.println("error"+e.getMessage());
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        
    }
    public void setMyEventListner(ReceiveEventListner listener){
        this.listener = listener;
    }   
    @Override
    public void messageArrived(String topic, MqttMessage msg) throws Exception {
        //System.out.println(topic+","+msg.toString());
        listener.recvMsg(topic, msg);
    }
}
