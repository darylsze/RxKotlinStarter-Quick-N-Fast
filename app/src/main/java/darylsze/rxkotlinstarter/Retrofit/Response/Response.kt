package darylsze.rxkotlinstarter.Retrofit.Response

/**
 * Created by windsze on 25/8/2017.
 * HKMovie. GT.
 */
data class User(
        val id: Int,
        val name: String,
        val username: String,
        val email: String,
        val address: AddressBean,
        val phone: String,
        val website: String,
        val company: CompanyBean)

data class AddressBean(
        val street: String,
        val suite: String,
        val city: String,
        val zipcode: String,
        val geo: GeoBean)

data class CompanyBean(
        val name: String,
        val catchPhrase: String,
        val bs: String)

data class GeoBean(
        val lat: String,
        val lng: String)

