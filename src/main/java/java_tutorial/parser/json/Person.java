package java_tutorial.parser.json;

public class Person {
	private String name;
	private int age;
	private String gender;

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}

	public Person(String name, int age, String gender) {
		this.name = name;
		this.age = age;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return String.format("Person [name=%s, age=%s, gender=%s]", name, age, gender);
	}


}
