package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerFilter {
    public static int DEFAULT_PAGE_NUMBER = 0;
    public static int DEFAULT_PAGE_SIZE = 3;

    public static List<Player> getFilteredPlayersList(List<Player> players, HttpServletRequest request, boolean isPagingUsed){
        List<Player> filteredPlayers = filterPlayersListByParams(players, request);

        sortPlayersList(filteredPlayers, request.getParameter("order"));

        if(isPagingUsed){
            filteredPlayers = filterPlayersListByPages(filteredPlayers,
                            request.getParameter("pageNumber"),
                            request.getParameter("pageSize"));
        }

        return filteredPlayers;
    }

    private static List<Player> filterPlayersListByParams(List<Player> players, HttpServletRequest request){
        List<Player> filteredPlayers = new ArrayList<>();

        for(Player player : players){
            String name = request.getParameter("name");
            if(name != null && !player.getName().contains(name))
                continue;

            String title = request.getParameter("title");
            if(title != null && !player.getTitle().contains(title))
                continue;

            String race = request.getParameter("race");
            if(race != null && player.getRace() != Race.valueOf(race))
                continue;

            String profession = request.getParameter("profession");
            if(profession != null && player.getProfession() != Profession.valueOf(profession))
                continue;

            String after = request.getParameter("after");
            if(after != null && Long.parseLong(after) > player.getBirthday().getTime())
                continue;

            String before = request.getParameter("before");
            if(before != null && Long.parseLong(before) < player.getBirthday().getTime())
                continue;

            String banned = request.getParameter("banned");
            if(banned != null && player.isBanned() != Boolean.valueOf(banned))
                continue;

            String minExperience = request.getParameter("minExperience");
            if(minExperience != null && player.getExperience() < Integer.parseInt(minExperience))
                continue;

            String maxExperience = request.getParameter("maxExperience");
            if(maxExperience != null && player.getExperience() > Integer.parseInt(maxExperience))
                continue;

            String minLevel = request.getParameter("minLevel");
            if(minLevel != null && player.getLevel() < Integer.parseInt(minLevel))
                continue;

            String maxLevel = request.getParameter("maxLevel");
            if(maxLevel != null && player.getLevel() > Integer.parseInt(maxLevel))
                continue;

            filteredPlayers.add(player);
        }

        return filteredPlayers;
    }

    private static List<Player> filterPlayersListByPages(List<Player> players, String pageNumberStr, String pageSizeStr) {
        Integer pageNumber = pageNumberStr == null
                        ? DEFAULT_PAGE_NUMBER + 1
                        : Integer.parseInt(pageNumberStr) + DEFAULT_PAGE_NUMBER + 1;
        
        Integer pageSize = pageSizeStr == null
                        ? DEFAULT_PAGE_SIZE
                        : Integer.parseInt(pageSizeStr);

        List<Player> resultPlayers = new ArrayList<>();
        int index1 = pageNumber * pageSize - pageSize;
        int index2 = pageNumber * pageSize;

        for(int i = index1; i < index2; i++){
            try{
                resultPlayers.add(players.get(i));
            }
            catch(IndexOutOfBoundsException e){
                break;
            }
        }

        return resultPlayers;
    }

    private static void sortPlayersList(List<Player> players, String order) {
        PlayerOrder playerOrder;

        if(order == null)
            playerOrder = PlayerOrder.ID;
        else
            playerOrder = PlayerOrder.valueOf(order);

        switch(playerOrder){
            case ID: players.sort(Comparator.comparing(Player::getId));
                break;
            case NAME: players.sort(Comparator.comparing(Player::getName));
                break;
            case EXPERIENCE: players.sort(Comparator.comparing(Player::getExperience));
                break;
            case BIRTHDAY: players.sort(Comparator.comparing(Player::getBirthday));
                break;
            case LEVEL: players.sort(Comparator.comparing(Player::getLevel));
                break;
        }
    }
}
