package ru.alastar.dolphin.events;

import java.util.ArrayList;

/**
 * Created by mick on 05.06.15.
 */
public class Event {

    private static ArrayList<EventHandler> _handlers = new ArrayList<EventHandler>();
    private static boolean cancelled = false;

    public static void register(EventHandler newH){
        if(!_handlers.contains(newH))
            _handlers.add(newH);
    }

    public static void fire(EventArguments args){
        cancelled = false;
        for(int i =  _handlers.size() - 1; !cancelled && i >= 0; --i)
        {
            _handlers.get(i).onEvent(args);
        }
    }

    public static void deregister(EventHandler eventHandler){
        if(_handlers.contains(eventHandler))
            _handlers.remove(eventHandler);
    }

    public static void cancel()
    {
        cancelled = true;
    }

}
