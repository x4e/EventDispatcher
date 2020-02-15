# EventDispatcher
A Kotlin implementation of Event Driven Architecture.

Example usage:
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
