package com.game.controller;

import com.game.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerValidator {
    public static boolean isNull(Player player){
        return player == null || player.getName() == null || player.getTitle() == null
                || player.getRace() == null || player.getProfession() == null
                || player.getBirthday() == null || player.getExperience() == null;
    }

    public static boolean isWrongPlayer(Player player){
        String name = player.getName();
        if(name != null && (name.isEmpty() || name.length() > 12))
            return true;

        String title = player.getTitle();
        if(title != null && title.length() > 30)
            return true;

        Integer experience = player.getExperience();
        if(experience != null && (experience < 0 || experience > 10_000_000))
            return true;

        try {
            Date birthday = player.getBirthday();
            if(birthday != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                int yearToCheck = Integer.parseInt(sdf.format(birthday));

                if(birthday.getTime() < 0 || yearToCheck < 2000 || yearToCheck > 3000)
                    return true;
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }
}
