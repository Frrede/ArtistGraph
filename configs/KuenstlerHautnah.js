{
    "name" : "Künstler Hautnah",
    "source" : {
        "url" : "http://www.arte.tv/de/kuenstler-hautnah-das-archiv/2995874,templateId=customListAjax,page=$n$,tab=tab__all,CmPart=com.arte-tv.www.html",
        "loops" : {
            "n" : {
                "from" : 1,
                "to" : 8
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : ".container_table_sort .content",
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "p.subtitle",
                            "modify" : function(result) {
                                return result.match(", [0-9]+.[0-9]{1}.([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "p.subtitle",
                            "modify" : function(result) {
                                return result.match(", [0-9]+.([0-9]{1}).[0-9]{4}")[1];
                            }
                        }
                    },
                    "parse" : [
                        {
                            "subSelectorAll" : "h3 > a",
                            "modify" : function(result) {
                                return result+"."; 
                            }
                        },
                        {
                            "subSelectorAll" : "p:last-of-type"
                        }
                    ]
                }
            ]
        }
    ]
}