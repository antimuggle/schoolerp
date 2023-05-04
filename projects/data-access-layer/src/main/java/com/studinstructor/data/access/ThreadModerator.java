package com.studinstructor.data.access;

import java.util.*;

import com.studinstructor.data.access.Moderator;

public interface ThreadModerator extends Moderator{
    public default void closeProblemThread(ProblemThread thread){};
    public default void openProblemThread(ProblemThread thread, ReplyThread solution){};
}