{
    "name" : "Kunstforum",
    "source" : {
        "url" : "http://www.kunstforum.de/uebersicht_baende_abb.asp",
    },
    "elements" : [
        {
            "selectorAll" : "#content > div:not(.clear)",
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "span",
                            "modify" : function(result) {
                                return result.match(", ([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "span",
                            "modify" : function(result) {
                                var ind = [null,1,null,2,null,3,null,4,null,1,2,3,1,2,3,1,2,3,4,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,null,2,3,1,2,3,4,5,6,null,7,8,9,1,2,3,4,5,6,null,7,8,9,10,11,1,2,null,3,null,4,5,6,null,1,2,3,4,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,1,2,3,4,5,6,7,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,1,2,3,4,1,2,3,4,1,2,3,4,5,1,2,3,4,5,1,2,3,4,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,1,2,3];
                                var max = [null,4,null,4,null,4,null,4,null,3,3,3,3,3,3,4,4,4,4,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,3,null,3,3,9,9,9,9,9,9,null,9,9,9,11,11,11,11,11,11,null,11,11,11,11,11,6,6,null,6,null,6,6,6,null,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,5,5,5,5,5,7,7,7,7,7,7,7,6,6,6,6,6,6,6,6,6,6,6,6,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,3,3,3,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,5,5,5,5,5,7,7,7,7,7,7,7,7,7,7,7,7,7,7,6,6,6,6,6,6,3,3,3];
                                
                                var nr = parseInt(result.match("([0-9]+), [0-9]{4}")[1]);
                                
                                var i = ind[nr];
                                var m = max[nr];
                                
                                return ""+Math.round(12 / m * i);
                            }
                        }
                    },
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "a",
                                "modify" : function(result) {
                                    return result.replace("uebersicht_baende_info.asp", "inhaltsverzeichnis.asp");
                                }
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#Inhalt",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "p",
                                                    "modify" : function(result) {
                                                        return result;
                                                    }
                                                },
                                                {
                                                    "subSelectorAll" : "h1, h2",
                                                    "modify" : function(result) {
                                                        return result + ".";
                                                    }
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