package gameengine.Observers;

import gameengine.Engine.GameObject;
import gameengine.Observers.Events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem
{
    private static List<Observer> observers = new ArrayList<>();
    public static void AddObserver(Observer observer)
    {
        observers.add(observer);
    }

    public static void Notify(GameObject obj, Event event)
    {
        for(Observer observer : observers)
        {
            observer.OnNotify(obj, event);
        }
    }
}
