package com.greenapp.dota2lounge.dota2lounge;



public class matches {
    String matchtime;
    String matchtournament;
    String team;
    String against;
    String teampersent;
    String againstpersent;
    String teampic;
    String againstpic;
    String backgrnd;
    String matchstatus;
    String matchwinner;
    String matchlink;



    public matches(String _matchtime,
                   String _matchtournament,
                   String _team,
                   String _against,
                   String _teampersent,
                   String _againstpersent,
                   String _teampic,
                   String _againstpic,
                   String _backgrnd,
                   String _matchstatus,
                   String _matchwinner,
                   String _matchlink
    ) {
        // TODO Auto-generated constructor stub
        matchtime=_matchtime;
        matchtournament=_matchtournament;
        team=_team;
        against=_against;
        teampersent=_teampersent;
        againstpersent=_againstpersent;
        teampic=_teampic;
        againstpic=_againstpic;
        backgrnd=_backgrnd;
        matchstatus=_matchstatus;
        matchwinner=_matchwinner;
        matchlink=_matchlink;

    }


}
