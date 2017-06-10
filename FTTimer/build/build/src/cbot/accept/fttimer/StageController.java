package cbot.accept.fttimer;


import cbot.accept.fttimer.view.FTimeEntryView;
import cbot.accept.fttimer.view.STimerView;
import cbot.accept.fttimer.view.TimeTable;
import javafx.stage.Stage;

public class StageController {

    public void SwitchScene(Stage primaryStage, String view, String userName) throws Exception {
        switch (view){
            case "FTimeEntry":
                FTimeEntryView ft = new FTimeEntryView(userName);
                ft.start(primaryStage);
                break;
            case "STTimer":
                STimerView st = new STimerView(userName);
                Stage newStage = new Stage();
                st.start(newStage);
                break;
            case "TimeTable":
                TimeTable timeTable = new TimeTable(userName);
                timeTable.start(primaryStage);
                break;
        }
    }
}
