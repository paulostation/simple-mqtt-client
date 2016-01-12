package src;


import java.util.Date;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Client {

	public static long timestamp;
	
	public static void main(String[] args) throws JSONException {

		Client sensor = new Client();
		// topic settings
		String deviceType = "device";
		String event = "telemetry";
		// device credentials for pauloh
		String deviceId = "pauloh";//test_device  vdss
		String deviceToken = "PY7qzifbAg8&@sHTCL";
                //device credentials for Vdss
//                String deviceId = "vdss";
//                String deviceToken = "60ZLWNHTK2o?7@n!YX";

		String orgID = "yr14xu";
		String apiKey = "use-token-auth";// a-b6kg16-jwxgexpmce
		//String apiToken = "ppaKT5JIalWSQpAR_e";
		 //String topic = "iot-2/type/" + deviceType + "/id/" + deviceId + "/evt/" + event + "/fmt/json";		
		String topic = "iot-2/evt/" + event + "/fmt/json";
		int qos = 0, timer = 30;
		int status = 0;		 
		String broker="tcp://"+ orgID + ".messaging.internetofthings.ibmcloud.com:1883";
		String clientId = "d:" + orgID + ":" + deviceType + ":" + deviceId;		
		MqttClient sampleClient = null;
		
		/*
		 * MemoryPersistence persistence = new MemoryPersistence(); try {
		 * persistence.wait(1200); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
		System.out.println("Device Token: " + deviceToken);
		System.out.println("Topic: " + topic);
		
		try {
			sampleClient = new MqttClient(broker, clientId);
			MqttConnectOptions connOpts = new MqttConnectOptions();

			connOpts.setCleanSession(true);
			connOpts.setConnectionTimeout(60 * 10);
			connOpts.setKeepAliveInterval(60 * 5);
			connOpts.setUserName(apiKey);
			connOpts.setPassword(deviceToken.toCharArray());
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");

			// for (int i = 0; i < timer; i++) {
			for(int i = 0; i < timer; i++){
			status = sensor.simSensor();// \"state\": \"Normal\""+ deviceId +"
			JSONObject content = new JSONObject();
			       content.put("id", deviceId);
			       content.put("type",deviceType);
				   content.put("name", deviceId);
				   content.put("lng", -46.649831);
				   content.put("lat",-23.597883);
				   content.put("sensor",status);
				   content.put("timestamp",getTimeStamp());				   
				   content.put("description", "LBS");

			System.out.println("Publishing message: " + content);
			MqttMessage message = new MqttMessage(content.toString().getBytes());
			message.setQos(qos);
			
			sampleClient.publish(topic, message);
			System.out.println("Message published");
		
			Thread.sleep(2000);
		}
			sampleClient.disconnect(30);
			System.out.println("Disconnected");
			System.exit(0);
			 
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				sampleClient.close();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public int simSensor() {
		
		int i = (int)(Math.random() * 1000) % 1000;		
		return i;

	}

	public static long getTimeStamp() {
		
		Date date = new Date();	        
        timestamp = date.getTime();
        System.out.println("Timestamp: " + timestamp);
        return timestamp;
	}
}