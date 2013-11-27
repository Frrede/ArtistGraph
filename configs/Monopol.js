{
    "name" : "Monopol",
    "source" : {
        "url" : "http://shop.monopol-magazin.de/einzelhefte?p=$i$",
        "loops" : {
            "i" : {
                "from" : 1,
                "to" : 4
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : "#products-list .item",
            "validate" : {
                "subSelector" : ".product-name > a",
                "regex" : ".*[0-9]{4}.*"
            },
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : ".product-name > a",
                            "modify" : function(result) {
                                return result.match("[a-zA-ZäüöÄÜÖ]+ +([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : ".product-name > a",
                            "modify" : function(result) {
                                var m = result.match("([a-zA-ZäüöÄÜÖ&;]+) +[0-9]{4}")[1];
                                switch (m.toLowerCase()) {
                                    case "januar":
                                        return "1";
                                    case "februar":
                                        return "2";
                                    case "m&auml;rz":
                                        return "3";
                                    case "april":
                                        return "4";
                                    case "mai":
                                        return "5";
                                    case "juni":
                                        return "6";
                                    case "juli":
                                        return "7";
                                    case "august":
                                        return "8";
                                    case "september":
                                        return "9";
                                    case "oktober":
                                        return "10";
                                    case "november":
                                        return "11";
                                    case "dezember":
                                        return "12";
                                }
                            }
                        }
                    },
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : ".product-name > a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#collateral-tabs",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : ".description"
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