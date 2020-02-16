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
class ExampleEvent: CancellableEvent()

object ApplicationEntryPoint {
	init {
		EventDispatcher.register(Listener);
		EventDispatcher.subscribe(Listener);
		EventDispatcher.dispatch(ExampleEvent());
	}
}

object Listener {
	@Subscriber
	private fun onExampleEvent(event: ExampleEvent) {
		println("Event Recieved!")
		event.isCancelled = true
	}
}
```

Java:
```Java
public class ExampleEvent extends CancellableEvent {}

public class ApplicationEntryPoint {
	public ApplicationEntryPoint() {
		Listener listener = new Listener();
		EventDispatcher.Companion.register(listener);
		EventDispatcher.Companion.subscribe(listener);
		EventDispatcher.Companion.dispatch(new ExampleEvent());
	}
}

public class Listener {
	@Subscriber
	private void onExampleEvent(ExampleEvent event) {
		System.out.println("Event Recieved");
		event.setCancelled(true);
	}
}
```