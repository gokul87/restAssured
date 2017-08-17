var faker = require('faker')

function generateData() {
	
	var store = []

    for(var id = 0; id <= 25; id++) {
        var authornamebook1 = faker.name.findName()
        var categorybook1 = faker.random.word()
        var authornamebook2 = faker.name.findName()
        var categorybook2 = faker.random.word()
        var city = faker.address.city()
        var titlebook1 = faker.random.word()
        var titlebook2 = faker.random.word()
        
        store.push({
            "id": id,
            "city": city,
            "store": {
            	"book":[
            		{
            			"author":authornamebook1,
            			"category":categorybook1,
            			"price":8.95,
            			"title":titlebook1
            		},
            		{
            			"author":authornamebook2,
                        "category":categorybook2,
                        "isbn":"0-553-21311-3",
                        "price":12.99,
                        "title":titlebook2
            		}
            	]
            } 
        }) 
    }

    return { "store": store }      
}

module.exports = generateData