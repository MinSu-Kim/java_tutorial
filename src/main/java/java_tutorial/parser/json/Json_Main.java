package java_tutorial.parser.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Json_Main {
	
	public static void main(String[] args) {
		// https://yookeun.github.io/java/2017/05/27/java-gson/ 曼炼
		change_objectToJson();

		make_simple_json();

		json_parsing();
		parsing_jsonToObject();
		
		jsonArrayToList();
		
		jsonArrayToList2();
	}

	private static void jsonArrayToList2() {
		Gson gson = new Gson(); 
		List<Person> list = new ArrayList<Person>();
		list.add(new Person("kim", 12, "Male"));
		list.add(new Person("kim1", 22, "FeMale"));
		list.add(new Person("kim2", 32, "Male"));
		
		String result ="";
		result = gson.toJson(list, new TypeToken<List<Person>>(){}.getType());
		System.out.println(result);
		
		List<Person> list2 = gson.fromJson(result, new TypeToken<List<Person>>(){}.getType()); 
		for(Person p : list2) {
			System.out.println(p);
		}
	}

	private static void jsonArrayToList() {
		Gson gson = new Gson(); 
		String jsonString = "[{'name':'kim','age':20,'age':26,'gender':'M'},{'name':'kim1','age':21,'age':26,'gender':'FM'},{'name':'kim2','age':20,'age':24,'gender':'M'}]"; // 规过1 
		//规过 1
		Person[] array = gson.fromJson(jsonString, Person[].class); 
		List<Person> list = Arrays.asList(array); 
		// 规过2 
		List<Person> list2 = gson.fromJson(jsonString, new TypeToken<List<Person>>(){}.getType()); 
		System.out.println(list); 
		System.out.println(list2);

	}

	private static void parsing_jsonToObject() {
		String json = "{\"name\":\"kim\",\"age\":20,\"gender\":\"M\"}";
		Gson gson = new Gson();
		Person person = gson.fromJson(json, Person.class);

		System.out.println("name = " + person.getName());
		System.out.println("age = " + person.getAge());
		System.out.println("gender = " + person.getGender());
	}

	private static void json_parsing() {
		String json = "{\"name\":\"kim\",\"age\":20,\"gender\":\"M\"}";
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		String name = element.getAsJsonObject().get("name").getAsString();
		System.out.println("name = " + name);
		int age = element.getAsJsonObject().get("age").getAsInt();
		System.out.println("age = " + age);
		String gender = element.getAsJsonObject().get("gender").getAsString();
		System.out.println("gender = " + gender);
	}

	private static void change_objectToJson() {
		Person p = new Person("kim", 20, "M");
		Gson gson = new Gson();
		String json = gson.toJson(p);
		System.out.println(json);
	}

	private static void make_simple_json() {
		System.out.println("make_simple_json()");
		Gson gson = new Gson();
		JsonObject object = new JsonObject();
		object.addProperty("name", "park");
		object.addProperty("age", 22);
		object.addProperty("success", true);
		String json = gson.toJson(object);
		System.out.println(json);
	}
}
