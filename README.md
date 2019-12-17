# EventDispatcher
An implementation of event based architecture, using annotations

I wanted this to be a very fast performing event bus, which is why I designed the register/subscribe architecture. Traditional event buses use reflection to scan the class each time it is subscribed. This event bus scans the class once, on registration, and then simply enables/disables to previously found methods on subscription/unsubscription. This means that the only times the performance impacting reflection api are used are once on registration, and on the actual dispatching of events.

Example usage:
```Java
/**
 * @author cookiedragon234 15/Dec/2019
 */
public class KeyPressedEvent extends AbstractEvent
{
	private final Key key;
	
	public KeyPressedEvent(Key key)
	{
		this.key = key;
	}
	
	public Key getKey()
	{
		return key;
	}
}
```

```Java
/**
 * @author cookiedragon234 15/Dec/2019
 */
@Mixin(Minecraft.class)
public class MixinMinecraft
{
	@Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
	private void onKeyPressWrapper(CallbackInfo ci)
	{
		if(Keyboard.getEventKeyState())
		{
			EventDispatcher.dispatch(new KeyPressedEvent(Key.fromCode(Keyboard.getEventKey())));
		}
	}
}
```

```Java
**
 * @author cookiedragon234 15/Dec/2019
 */
public class BindManager
{
	public static void init()
	{
		EventDispatcher.register(BindManager.class);
		EventDispatcher.subscribe(BindManager.class);
	}
	
	@Subscriber
	private static void onKeyPress(KeyPressedEvent event)
	{
		for(Module module : ModuleManager.getModules())
		{
			if(module.getKeyBind().getValue() == event.getKey())
			{
				module.toggle();
			}
		}
	}
}
```
