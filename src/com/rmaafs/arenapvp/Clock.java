package com.rmaafs.arenapvp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rmaafs.arenapvp.Extra.clang;
import static com.rmaafs.arenapvp.Extra.jugandoUno;
import static com.rmaafs.arenapvp.Extra.scores;

import com.rmaafs.arenapvp.Juegos.Meetup.GameMeetup;
import com.rmaafs.arenapvp.Party.DuelGame;
import com.rmaafs.arenapvp.Party.EventGame;
import com.rmaafs.arenapvp.Party.Party;

import static com.rmaafs.arenapvp.Main.meetupControl;
import static com.rmaafs.arenapvp.Main.partyControl;
import static com.rmaafs.arenapvp.Main.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Clock {

    public Clock() {
        setConfig();
    }

    public String startingGame;
    public List<String> startingDuelStart;

    public void setConfig() {
        startingGame = Extra.tc(clang.getString("starting.game"));
        startingDuelStart = Extra.tCC(clang.getStringList("starting.duel.start"));
        time();
    }

    private void time() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
                new Runnable() {
                    int sec = 0;
                    int day = 0;

                    @Override
                    public void run() {
                        sec++;
                        if (sec == 20) {
                            try {
                                segundo();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            day++;
                            if (day > 600) {
                                day = -1;
                                Extra.detectNextDay();
                            }
                            sec = -1;
                        }
                    }
                }, 0, 1L);
    }

    private void segundo() {
        updateScore();
        preStartDuel();
        removeSecondOnGame();
        removeSecondOnPreMeetups();
        removeSecondOnPrePartyEvents();
        removeSecondOnPrePartyDuels();

        removeSecondOnMeetups();
        removeSecondOnPartyDuel();
        removeSecondOnPartyEvent();
    }

    public void updateScore() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (scores.containsKey(p)) {
                scores.get(p).update();
            }
        }
    }

<<<<<<< Updated upstream:src/com/rmaafs/arenapvp/Reloj.java
    private void preEmpezarDuels() {
        List<Partida> terminadas = new ArrayList<>();
        for (Partida partida : Extra.preEmpezandoUno) {
            if (partida.pretime == 0) {
                partida.startGame(startingDuelStart);
                terminadas.add(partida);
=======
    private void preStartDuel() {
        List<Game> ended = new ArrayList<>();
        for (Game game : Extra.preEmpezandoUno) {
            if (game.preTime == 0) {
                game.startGame(startingDuelStart);
                ended.add(game);
>>>>>>> Stashed changes:src/com/rmaafs/arenapvp/Clock.java
            } else {
                partida.starting(startingGame.replaceAll("<time>", "" + partida.pretime));
                partida.pretime--;
            }
        }
        Extra.preEmpezandoUno.removeAll(ended);
    }

<<<<<<< Updated upstream:src/com/rmaafs/arenapvp/Reloj.java
    private void quitarSegundoPartidas() {
        List<Partida> editadas = new ArrayList<>();
        for (Map.Entry<Player, Partida> entry : jugandoUno.entrySet()) {
            Partida partida = entry.getValue();
            if (!editadas.contains(partida)) {
                partida.removerSec();
                editadas.add(partida);
=======
    private void removeSecondOnGame() {
        List<Game> editing = new ArrayList<>();
        for (Map.Entry<Player, Game> entry : jugandoUno.entrySet()) {
            Game game = entry.getValue();
            if (!editing.contains(game)) {
                game.removerSec();
                editing.add(game);
>>>>>>> Stashed changes:src/com/rmaafs/arenapvp/Clock.java
            }
        }
    }

    private void removeSecondOnMeetups() {
        List<GameMeetup> editing = new ArrayList<>();
        for (Map.Entry<Integer, GameMeetup> entry : meetupControl.meetups.entrySet()) {
            GameMeetup gameMeetup = entry.getValue();
            if (!editing.contains(gameMeetup)) {
                gameMeetup.removerSec();
                editing.add(gameMeetup);
            }
        }
    }

    private void removeSecondOnPartyDuel() {
        List<DuelGame> editing = new ArrayList<>();
        for (Map.Entry<Party, DuelGame> entry : partyControl.partysDuel.entrySet()) {
            DuelGame duelGame = entry.getValue();
            if (!editing.contains(duelGame)) {
                duelGame.removerSec();
                editing.add(duelGame);
            }
        }
    }

    private void removeSecondOnPartyEvent() {
        List<EventGame> editing = new ArrayList<>();
        for (Map.Entry<Party, EventGame> entry : partyControl.partysEvents.entrySet()) {
            EventGame eventGame = entry.getValue();
            if (!editing.contains(eventGame)) {
                eventGame.removerSec();
                editing.add(eventGame);
            }
        }
    }

    private void removeSecondOnPreMeetups() {
        List<GameMeetup> started = new ArrayList<>();
        for (GameMeetup game : meetupControl.meetupStarting) {
            if (game.removePretime()) {
                game.start();
                started.add(game);
            }
        }
        meetupControl.meetupStarting.removeAll(started);
    }

    private void removeSecondOnPrePartyEvents() {
        List<EventGame> started = new ArrayList<>();
        for (EventGame game : partyControl.startingsEvents) {
            if (game.removePretime()) {
                game.start();
                started.add(game);
            }
        }
        partyControl.startingsEvents.removeAll(started);
    }

    private void removeSecondOnPrePartyDuels() {
        List<DuelGame> started = new ArrayList<>();
        for (DuelGame game : partyControl.startingPartyDuel) {
            if (game.removePretime()) {
                game.start();
                started.add(game);
            }
        }
        partyControl.startingPartyDuel.removeAll(started);
    }
}
