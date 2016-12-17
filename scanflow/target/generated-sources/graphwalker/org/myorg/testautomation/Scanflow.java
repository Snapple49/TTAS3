// Generated by GraphWalker (http://www.graphwalker.org)
package org.myorg.testautomation;

import org.graphwalker.java.annotation.Model;
import org.graphwalker.java.annotation.Vertex;
import org.graphwalker.java.annotation.Edge;

@Model(file = "org/myorg/testautomation/Scanflow.graphml")
public interface Scanflow {

    @Vertex()
    void AS_Open();

    @Edge()
    void idle();

    @Edge()
    void invalid_sign_on();

    @Edge()
    void invalid_art_reg();

    @Edge()
    void valid_get();

    @Edge()
    void valid_sign_on();

    @Vertex()
    void Disconnected();

    @Edge()
    void invalid_trans();

    @Edge()
    void invalid_sign_off();

    @Vertex()
    void AS_Idle();

    @Vertex()
    void AS_Closed();

    @Vertex()
    void Not_Signed();

    @Edge()
    void valid_sign_off();

    @Edge()
    void close_acc();

    @Vertex()
    void AS_Ending();

    @Edge()
    void invalid_get();

    @Edge()
    void valid_art_reg();

    @Edge()
    void open_acc();

    @Edge()
    void valid_trans();

    @Edge()
    void connect();
}
