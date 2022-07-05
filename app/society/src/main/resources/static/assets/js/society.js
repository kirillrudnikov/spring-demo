//Show Password Script
// {
//     function showPassword() {
//
//         let htmlElement = document.querySelectorAll(".password-show");
//
//         if (htmlElement.type === "password") {
//             htmlElement.type = "text";
//         } else {
//             htmlElement.type = "password";
//         }
//
//     }
//
// }
{
    var pwShown = 0;

    document.getElementById("password-control").addEventListener("click", function () {
        if (pwShown === 0) {
            pwShown = 1;
            document.getElementById('password').setAttribute('type', 'text')
        } else {
            pwShown = 0;
            document.getElementById('password').setAttribute('type', 'password')
        }
    }, false);
}
// {
//     function showPassword(id, element) {
//
//         let htmlElement = document.getElementById(id);
//
//         if (htmlElement.type === "password") {
//             htmlElement.type = "text";
//         } else {
//             htmlElement.type = "password";
//         }
//
//     }
//
// }

// Format Phone Number Script
{
    let phoneElement = document.querySelector('#phoneNumber')

    phoneElement.addEventListener("keyup", (event) => {
        let val = event.target.value;
        let pattern = /(\+7|8)[\s(]?(\d{3})[\s)]?(\d{3})[\s-]?(\d{2})[\s-]?(\d{2})/g;
        event.target.value = val.replace(pattern, '+7 ($2) $3-$4-$5');
    })
}