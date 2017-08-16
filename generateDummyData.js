var faker = require('faker')

function generateData() {

    var personal = []

    for(var id = 0; id <= 50; id++) {
        var firstname = faker.name.firstName()
        var lastname = faker.name.lastName()
        var city = faker.address.city()
        var housetype = faker.lorem.word()
        var minValue = faker.random.number()
        var maxValue = faker.random.number()
        var date = faker.date.past()

        personal.push({
            "id": id,
            "firstname": firstname,
            "secondname": lastname,
            "city": city,
            "search": {
                "Housetype": "independent",
                "location": "London",
                "minvalue": 29033,
                "maxvalue": 90017
            }          
        }) 
    }

    return { "personal": personal }      
}

module.exports = generateData