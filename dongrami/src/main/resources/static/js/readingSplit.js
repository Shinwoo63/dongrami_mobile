document.addEventListener('DOMContentLoaded', function() {
    var content = document.getElementById('readingContent');
    if (content) {
        var text = content.textContent;
        var paragraphs = text.split(/(?<=\. )/); // 문장 끝(마침표+공백) 기준으로 분리
        content.innerHTML = paragraphs.map(p => `<p>${p.trim()}</p>`).join('');
    }
});
