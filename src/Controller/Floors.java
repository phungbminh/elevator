//created by sch001, 2019/4/15

package Controller;

import Elevator.Elevator;
import Utils.BaseButton;
import Utils.FloorFLags;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

class OuterButton extends BaseButton{
    private static String[] buttonFlags = new String[]{"🔻", "🔺"};

    private int floor;//楼层
    //1 nghĩa là tăng, 0 nghĩa là giảm
    private int direction;//Các nút chỉ có thể lên hoặc xuống
    OuterButton(int floor, int up){
        super(buttonFlags[up]);
        this.floor = floor;
        this.direction = up;
    }

    int getFloor(){
        return floor;
    }
    int getDirection(){
        return direction;
    }
}

//Nút bấm cho nhiều tầng
public class Floors extends JPanel{
    //UI

    private OuterButton[] buttons = new OuterButton[Config.MaxFloor * 2];

    private JLabel[] floorFlags = new JLabel[Config.MaxFloor];
    private Controller controller;

//    TODO: paintComponent()

    public Floors(Controller controller){
        this.controller = controller;
        controller.setFloors(this);

        setLayout(null);
        for(int i = 1; i< Config.MaxFloor * 2 - 1; i++){
            buttons[i] = new OuterButton(Config.MaxFloor - i / 2,(i + 1) % 2);
            //init
            buttons[i].setMargin(new Insets(1,1,1,1));
            buttons[i].setFont(new Font(buttons[i].getFont().getFontName(), buttons[i].getFont().getStyle(),15));
            buttons[i].setBounds((i % 2 + 1) * (Config.floorButtonWidth+Config.floorButtonSpace),
                                 (i / 2)*(Config.floorButtonSpace+Config.floorButtonHeight),
                                    //0,0,
                                    Config.floorButtonWidth,
                                    Config.floorButtonHeight
                    );
//            System.out.println((i%2)*(Config.floorButtonWidth+Config.floorButtonSpace));
//            System.out.println((i/2+1)*(Config.floorButtonSpace+Config.floorButtonHeight));
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setForeground(Color.BLACK);
            buttons[i].addActionListener(buttonListener);

            this.add(buttons[i]);
        }

        FloorFLags.addFloorFLags(floorFlags,this, Config.floorFlagStart);
    }
    //TODO: MOdify this fuction to commit a Task
    ActionListener buttonListener= event -> {
        OuterButton pressButton=(OuterButton) event.getSource();
//        System.out.println(pressButton.getFloor()+" "+pressButton.getDirection());
        pressButton.turnon();
        controller.commitTask(new Task(pressButton.getFloor(),pressButton.getDirection()));
    };

    public void turnoffLight(int floor, int up){
        System.out.println("Turn off" + ((1-up) + 2*(floor - 1)));
        buttons[(Config.MaxFloor - floor)*2+(1-up)].turnoff();
    }
}

//
