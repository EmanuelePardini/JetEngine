package gameengine.Observers;

import gameengine.Engine.GameObject;
import gameengine.Observers.Events.Event;

public interface Observer
{
    void OnNotify(GameObject object, Event event);
}
