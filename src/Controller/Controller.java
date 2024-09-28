//created by sch001, 2019/4/15

package Controller;

//import java.util.LinkedList;
import Elevator.Elevator;
import Elevator.Elevator.*;

import java.util.LinkedList;
import java.util.Queue;

/*TODO: 理清synchronized 的逻辑，在这里实现Task到InnerTask的转化，并投递到电梯 */
public class Controller extends Thread{
    //Sở hữu thang máy
    Elevator[] elevators;
    //hàng đợi nhiệm vụ
    Queue<Task> tasks = new LinkedList<Task>();
    //Thang máy được lên lịch cuối cùng để các nhiệm vụ được phân bổ đồng đều nhất có thể
    int lastElevator = 0;

    Floors floors;

    public void setFloors(Floors floors) {
        this.floors = floors;
    }

    public Controller(Elevator[] elevators){
        this.elevators = elevators;
        for(int i = 0; i < Config.ElevatorNum; i++){
            this.elevators[i].setController(this);
        }
    }
    //Chọn thang máy tốt nhất dựa trên nhiệm vụ được giao
    @Override
    public void run(){
        //Bỏ phiếu, ngủ khi không có nhiệm vụ
        while (true){
            if(tasks.isEmpty()){
                try{
                    sleep(1000);
                }catch (InterruptedException e){
                    System.out.println("Controller!");
                }
                continue;
            }
            //Sắp xếp Task vào thang máy phù hợp
            while (!tasks.isEmpty()){
                Task task = tasks.remove();
                schedule(task);
            }
        }
    }
    //
    public synchronized void commitTask(Task task){
        tasks.add(task);
    }

    //Lập kế hoạch logic
    void schedule(Task task){
        int start = lastElevator;
        int distance = Config.MaxFloor + 1;//Khoảng cách giữa thang máy ứng viên và hành khách được đón

        for(int i = 0; i < Config.ElevatorNum; i++){
            start = (start+1)%Config.ElevatorNum;
            if(elevators[start].getELeState() == task.direction){
                if(task.direction == EleState.UP && task.floor>=elevators[start].getFloor()||
                    task.direction == EleState.DOWN && task.floor<=elevators[start].getFloor()
                ){
                    lastElevator = start;
                    break;
                }
            }
            if(elevators[start].getELeState() == EleState.STALL){
                int ndistance = Math.abs(elevators[start].getFloor() - task.floor);
                if(distance > ndistance){
                    lastElevator = start;
                    distance = ndistance;
                }
            }
        }
//        lastElevator = start;
        elevators[lastElevator].commitOuterTask(task.floor, task.direction);
    }

    //Tắt đèn
    public void turnoffLight(int floor, int up){
        floors.turnoffLight(floor, up);
    }
}
