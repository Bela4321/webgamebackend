package com.belaschinke.webgamebackend.service.messageProtocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MessageParser {

    public static  <T> T parseMessage(String message, Class<T> type) {
        String[] messageArray = message.split(";");
        Map<String,String> attributes = new HashMap<>();
        for (String attribute : messageArray) {
            String[] attributeArray = attribute.split(",");
            attributes.put(attributeArray[0], attributeArray[1]);
        }

        T instance = null;
        try {
            Constructor<T> constructor = type.getConstructor();
            instance = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            //field types: int, long , boolean, String
            try {
                if (field.getType().equals(int.class)) {
                    field.set(instance, Integer.parseInt(attributes.get(field.getName())));
                } else if (field.getType().equals(long.class)) {
                    field.set(instance, Long.parseLong(attributes.get(field.getName())));
                } else if (field.getType().equals(boolean.class)) {
                    field.set(instance, Boolean.parseBoolean(attributes.get(field.getName())));
                } else if (field.getType().equals(String.class)) {
                    field.set(instance, attributes.get(field.getName()));
                } else {
                    System.out.println("Field type not supported");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
