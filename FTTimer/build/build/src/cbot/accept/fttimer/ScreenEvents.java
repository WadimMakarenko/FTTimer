package cbot.accept.fttimer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;


public class ScreenEvents {

    public static class OnMouseClicked implements EventHandler {
        private String toNextView = null;
        private Stage theStage = null;
        private String UserName = null;

        public OnMouseClicked(Stage theStage, String toNextView, String UserName) {
            this.toNextView = toNextView;
            this.theStage = theStage;
            this.UserName = UserName;
        }
        @Override
        public void handle(Event event) {
            StageController switchSc = new StageController();
            try {
                switchSc.SwitchScene(theStage, toNextView, UserName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
