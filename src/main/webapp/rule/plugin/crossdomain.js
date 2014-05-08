var CrossDomain = function () {
    this.visit = function (args) {
        var oldEle = document.getElementById('cross_domain_transfer');
        oldEle && document.body.removeChild(oldEle);
        var iframe = document.createElement('iframe');
        iframe.style.width = 0;
        iframe.style.height = 0;
        iframe.style.border = 0;
        iframe.src = 'http://134.102.170.45:8083/sfz/cross_domain_transfer.html?args=' + encodeURIComponent(args);
        document.body.appendChild(iframe);
    }

}