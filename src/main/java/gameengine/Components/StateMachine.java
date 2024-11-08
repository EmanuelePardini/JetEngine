package gameengine.Components;

import imgui.ImGui;
import imgui.callback.ImStrConsumer;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.lwjgl.system.linux.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//TODO: Refactor private and public variables

public class StateMachine extends Component
{
    private class StateTrigger
    {
        public String state;
        public String trigger;

        public StateTrigger() {}
        public  StateTrigger(String state, String trigger)
        {
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object o)
        {
            if(o.getClass() != StateMachine.class) return false;
            StateTrigger t2 = (StateTrigger)o;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(trigger,state);
        }
    }

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void RefreshTexture()
    {
        for(AnimationState state : states)
        {
            state.RefreshTexture();
        }
    }
    public void AddStateTrigger(String from, String to, String onTrigger)
    {
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void AddState(AnimationState state)
    {
        this.states.add(state);
    }

    public void SetDefaultState(String animationTitle)
    {
        for(AnimationState state : states)
        {
            if(state.title.equals(animationTitle))
            {
                defaultStateTitle = animationTitle;

                if(currentState == null)
                {
                    currentState = state;
                    return;
                }
            }
        }
        System.out.println("Unable to find state " + animationTitle);
    }

    public void Trigger(String trigger)
    {
        for(StateTrigger state : stateTransfers.keySet())
        {
            if(state.state.equals(currentState.title) && state.trigger.equals(trigger))
            {
                if(stateTransfers.get(state) != null)
                {
                    int newStateIndex = -1;
                    int index = 0;
                    for(AnimationState s : states)
                    {
                        if(s.title.equals(stateTransfers.get(state)))
                        {
                            newStateIndex = index;
                            break;
                        }
                        index++;
                    }
                    if(newStateIndex > -1)
                    {
                        currentState = states.get(newStateIndex);

                    }
                }
                return;
            }
        }
        System.out.println("Unable to find Trigger: " + trigger);
    }

    @Override
    public void Start()
    {
        for(AnimationState state : states)
        {
            if(state.title.equals(defaultStateTitle))
            {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void Update(float DeltaTime)
    {
        if(currentState != null)
        {
            currentState.Update(DeltaTime);
            SpriteRenderer sprite = gameObject.GetComponent(SpriteRenderer.class);
            if(sprite != null)
            {
                sprite.SetSprite(currentState.GetCurrentSprite());
            }
        }
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        if(currentState != null)
        {
            currentState.Update(DeltaTime);
            SpriteRenderer sprite = gameObject.GetComponent(SpriteRenderer.class);
            if(sprite != null)
            {
                sprite.SetSprite(currentState.GetCurrentSprite());
            }
        }
    }

    @Override
    public void ImGui()
    {
        int index = 0;

        for(AnimationState state : states)
        {
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            ImBoolean doesLoop = new ImBoolean(state.doesLoop);
            ImGui.checkbox("Does Loop: ", doesLoop);
            state.SetLoop(doesLoop.get());

            for(Frame frame : state.animationFrames)
            {
                float[] tmp = new float[1];
                tmp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time: ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }
        }
    }
}
