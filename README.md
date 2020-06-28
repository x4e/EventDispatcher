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
data class ExampleEvent(var str: String)

object Test {
	init {
		EventDispatcher.register { event: ExampleEvent ->
			println("Event Received!")
			event.str = "b"
		}
		with (ExampleEvent("a")) {
			EventDispatcher.dispatch(this)
			println(str) // "b"
		}
	}
}
```

Java:
```Java
public class Test {
	static class Event {
		public String str;
		public Event(String str) {
			this.str = str;
		}
	}
	
	static {
		EventDispatcher.register(Event.class, event -> {
			System.out.println("Event Received!");
			event.str = "b";
			return Unit.INSTANCE;
		});
		Event event = new Event("a");
		EventDispatcher.dispatch(event);
		System.out.println(event.str); // "b"
	}
}
```
