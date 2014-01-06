import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class ZabbixApiClient {
	private static String ZABBIX_API_URL = "http://1.2.3.4/zabbix/api_jsonrpc.php"; // 1.2.3.4 is your zabbix_server_ip
	
	public static void testClient() throws JSONException, FileNotFoundException, UnsupportedEncodingException {
	
		HttpClient client = new HttpClient();
		
		PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
		putMethod.setRequestHeader("Content-Type", "application/json-rpc"); // content-type is controlled in api_jsonrpc.php, so set it like this
		
		// create json object for apiinfo.version 
		JSONObject jsonObj=new JSONObject("{\"jsonrpc\":\"2.0\",\"method\":\"apiinfo.version\",\"params\":[],\"auth\":\"a6e895b98fde40f4f7badf112fd983bf\",\"id\":2}");
		
		putMethod.setRequestBody(fromString(jsonObj.toString())); // put the json object as input stream into request body 
		
		System.out.println("jsonObj:\n"+jsonObj);
		
		String loginResponse = "";
		
		try {
			client.executeMethod(putMethod); // send to request to the zabbix api
			
			loginResponse = putMethod.getResponseBodyAsString(); // read the result of the response
			
			System.out.println("loginResponse: \n"+loginResponse); // print the result of the response
			
			// Work with the data using methods like...
			JSONObject obj = new JSONObject(loginResponse); 
			String id = obj.getString("id");
			String result = obj.getString("result");
			System.out.println("id:"+id);
			System.out.println("result:"+result);
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static InputStream fromString(String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes("UTF-8");
		return new ByteArrayInputStream(bytes);
	}

	
	public static void main(String[] args) {
		try {
			testClient();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}


private String authenticate(){
		
		Authenticate authenticate = new Authenticate(zabbixUser,zabbixPassword);
		ZabbixCallAPI<Authenticate> zabbixCallAPI = new ZabbixCallAPI<Authenticate>();
		zabbixCallAPI.setAuth(null);
		zabbixCallAPI.setMethod("user.authenticate");
		zabbixCallAPI.setParams(authenticate);
		String json = gson.toJson(zabbixCallAPI,new TypeToken<ZabbixCallAPI<Authenticate>>() {}.getType());
		String response = this.callPost(json);
		ZabbixResult<String> zabbixResult  = new ZabbixResult<String>();
		zabbixResult = gson.fromJson(response, new TypeToken<ZabbixResult<String>>() {}.getType());
		return zabbixResult.getResponse();
}

public class ZabbixCallAPI<T> {

	private String jsonrpc = "2.0";
	
	private String auth;
	
	private String method;

	private T params;
	
	private int id = 0;
	
	public ZabbixCallAPI(String auth,String method,T params){
		this.auth = auth;
		this.method = method;
		this.params = params;
	}
	public ZabbixCallAPI(){
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	private String id;
	
	public ZabbixResult(){
		
	}
	
	public ZabbixResult(T result){
		this.result = result;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getResponse() {
		return result;
	}

	public void setResponse(T response) {
		this.result = response;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

 private String callPost(String content) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(zabbixAPIURL));

		postMethod.addRequestHeader("Content-Type", "application/json-rpc");
		String response = null;
		try {
			StringRequestEntity stringRequestEntity = new StringRequestEntity(content,"application/json", "UTF-8");
			postMethod.setRequestEntity(stringRequestEntity);
			httpClient.executeMethod(postMethod);
			response = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} 

		postMethod.releaseConnection();
		return response;
	}
