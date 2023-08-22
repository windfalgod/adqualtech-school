const host = "https://provinces.open-api.vn/api/";
let callAPI = (api) => {
    return axios.get(api)
    .then((response) => {
        renderData(response.data, "citySelect");
    });
}

callAPI('https://provinces.open-api.vn/api/?depth=1');
let callApiDistrict = (api) => {
    return axios.get(api)
    .then((response) => {
        renderData(response.data.districts, "districtSelect");
    });
}

let callApiWard = (api) => {
    return axios.get(api)
    .then((response) => {
    renderData(response.data.wards, "wardSelect");
    });
}

let renderData = (array, select) => {
    let row = ' <option disable value="">Chọn</option>';
    array.forEach(element => {
        row += `<option data-id="${element.code}" value="${element.name}">${element.name}</option>`
    });
    document.querySelector("#" + select).innerHTML = row
}

$("#citySelect").change(() => {
    callApiDistrict(host + "p/" + $("#citySelect").find(':selected').data('id') + "?depth=2");
});
    $("#districtSelect").change(() => {
    callApiWard(host + "d/" + $("#districtSelect").find(':selected').data('id') + "?depth=2");
});