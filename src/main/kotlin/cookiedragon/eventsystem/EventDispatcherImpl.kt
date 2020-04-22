package cookiedragon.eventsystem

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

/**
 * @author cookiedragon234 15/Feb/2020
 */
internal object EventDispatcherImpl: EventDispatcher {
	private val subscriptions: MutableMap<Class<*>, MutableSet<SubscribingMethod>> = ConcurrentHashMap()
	
	override fun <T : Any> dispatch(event: T): T {
		var clazz: Class<*> = event.javaClass
		while (true) {
			subscriptions[clazz]?.let { methods ->
				for (method in methods) {
					if (method.active) {
						method.invoke(event)
					}
				}
			}
			if (clazz == Any::class.java)
				break
			clazz = clazz.superclass

		}
		return event
	}
	
	override fun register(subscriber: Class<*>) = register(subscriber, null)
	
	override fun register(subscriber: Any) = register(subscriber.javaClass, subscriber)
	
	private fun register(clazz: Class<*>, instance: Any?) {
		for (method in clazz.declaredMethods) {
			// If we are registering a static class then only allow static methods to be indexed
			if (instance == null && !method.isStatic())
				continue
			
			// If we are registering an initialised class then skip static methods
			if (instance != null && method.isStatic())
				continue
			
			// Needs Subscriber annotation
			if (!method.isAnnotationPresent(Subscriber::class.java))
				continue
			
			if (method.parameterCount != 1) {
				IllegalArgumentException("Expected only 1 parameter for $clazz.${method.name}")
						.printStackTrace()
				continue
			}
			method.isAccessible = true

			val eventType = method.parameterTypes[0]!!
			val methodHandle = MethodHandles.lookup().unreflect(method)

			subscriptions.getOrPut(
				eventType, {
					hashSetOf()
				}
			).add(SubscribingMethod(clazz, instance, methodHandle))
		}
	}
	
	
	override fun subscribe(subscriber: Class<*>) = setActive(subscriber, true)
	
	override fun subscribe(subscriber: Any)  = setActive(subscriber, true)
	
	override fun unsubscribe(subscriber: Class<*>)  = setActive(subscriber, false)
	
	override fun unsubscribe(subscriber: Any)  = setActive(subscriber, false)
	
	private fun setActive(instance: Any?, active: Boolean) {
		for (methods in subscriptions.values) {
			for (method in methods) {
				if (method.instance == instance) {
					method.active = active
				}
			}
		}
	}
	
	private fun setActive(subscriber: Class<*>, active: Boolean) {
		for (methods in subscriptions.values) {
			for (method in methods) {
				if (method.clazz == subscriber) {
					method.active = active
				}
			}
		}
	}
}

data class SubscribingMethod(val clazz: Class<*>, val instance: Any?, val method: MethodHandle, var active: Boolean = false) {

	
	@Throws(Throwable::class)
	fun invoke(event: Any) {
		method.invoke(this.instance, event)
	}
}

private fun Method.isStatic() = Modifier.isStatic(this.modifiers)
