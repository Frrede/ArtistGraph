{
    "name" : "LIDO",
    "source" : {
        "url" : "http://www.br.de/service/suche/suche104.html?documentTypes=broadcast&page=$p$&query=%22LIDO%22&resultsPerPage=40&sort=date",
        "loops" : {
            "p" : {
                "from" : 1,
                "to" : 5
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : ".search_result",
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "p.search_date",
                            "modify" : function(result) {
                                return result.match("[0-9]{2}.[0-9]{2}.([0-9]{4}) \\|")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "p.search_date",
                            "modify" : function(result) {
                                var i = result.match("[0-9]{2}.([0-9]{2}).[0-9]{4} \\|")[1];
                                return ""+parseInt(i);
                            }
                        }
                    },
                    "parse" : [
                        {
                            "subSelectorAll" : "p:not(.search_date)"
                        }
                    ],
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "h3 > a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#content",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : ".detail > .detail_inlay > p"
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