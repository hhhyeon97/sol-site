// common.js

// 페이지 로드 시 실행되는 함수
window.onload = function() {
    // 세션 스토리지에서 닉네임을 가져와서 네비게이션바에 표시
    var displayName = localStorage.getItem('displayName');
    if (displayName) {
        var loginUserName = document.querySelector('.login-user-name');
        loginUserName.textContent = '🍃 ' + displayName + '님';
        console.log(loginUserName);
    }
};