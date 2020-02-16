# EventDispatcher
A Kotlin implementation of Event Driven Architecture.

Example usage:

build.gradle:
```Groovy
repositories {
	maven {
		url 'https://jitpack.io'
	}
}
dependencies {
	compile 'com.github.cookiedragon234:EventDispatcher:master-SNAPSHOT'
}
```

Kotlin:
```Kotlin
data class ExampleEvent(val str: String)

object ApplicationEntryPoint {
	init {
		EventDispatcher.register(Listener);
		EventDispatcher.subscribe(Listener);
		with (ExampleEvent("a")) {
			EventDispatcher.dispatch(this);
			println(str); // "b"
		}
	}
}

object Listener {
	@Subscriber
	private fun onExampleEvent(event: ExampleEvent) {
		println("Event Recieved!")
		event.str = "b";
	}
}
```

Java:
```Java
public class ExampleEvent {
	private String str;
	
	public ExampleEvent(String str) {
		this.str = str;
	}
	
	public String getStr() { return str; }
	public String setStr(String str) { this.str = str; }
}

public class ApplicationEntryPoint {
	public ApplicationEntryPoint() {
		Listener listener = new Listener();
		EventDispatcher.Companion.register(listener);
		EventDispatcher.Companion.subscribe(listener);
		ExampleEvent event = new ExampleEvent("a");
		EventDispatcher.Companion.dispatch(event);
		System.out.println(event.getStr()); // "b"
	}
}

public class Listener {
	@Subscriber
	private void onExampleEvent(ExampleEvent event) {
		System.out.println("Event Recieved");
		event.setStr("b");
	}
}
```