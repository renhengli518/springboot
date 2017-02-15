package com.renhengli.rabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @author renhengli
 *
 */
//@Component
//@RabbitListener(queues = "hello")
public class Receiver {

	//接收到消息处理.
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver--- : " + hello);
    }

    public static void main(String args []){
    	Receiver rec = new Receiver();
    	rec.process("hello1");
    }

}