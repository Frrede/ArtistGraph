{
    "name" : "Aspekte",
    "source" : {
        "url" : "http://www.zdf.de/ZDFmediathek/kanaluebersicht/aktuellste/500?teaserListIndex=1040&flash=off"
    },
    "elements" : [
        {
            "selectorAll" : "div.beitragListe",
            "tasks" : [
                {
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "div.text > p > b > a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#content",
                                    "tasks" : [
                                        {
                                            "date" : {
                                                "year" : {
                                                    "subSelector" : "div.beitrag > p:eq(0)",
                                                    "modify" : function(result) {
                                                        return result.match("[0-9]{2}.[0-9]{2}.([0-9]{4})")[1];
                                                    }
                                                },
                                                "month" : {
                                                    "subSelector" : "div.beitrag > p:eq(0)",
                                                    "modify" : function(result) {
                                                        var i = result.match("[0-9]{2}.([0-9]{2}).[0-9]{4}")[1];
                                                        return ""+parseInt(i);
                                                    }
                                                }
                                            },
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "h1.beitragHeadline",
                                                    "modify" : function(result) {
                                                        return result + ".";
                                                    }
                                                },
                                                {
                                                    "subSelectorAll" : "p.kurztext"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
}