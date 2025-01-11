'use strict';
// BlogApiController 에서 정의한 엔드포인트를 사용하는 자바스크립트
/**
 * 삭제 기능을 담당하는 코드입니다.
 * deleteButton 요소를 찾아 클릭 이벤트 리스너를 추가합니다.
 * 클릭 시 해당 글의 ID를 가져와서 DELETE 요청을 보냅니다.
 * BlogApiController의 @DeleteMapping("/api/articles/{id}") 로 전송
 */
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    /**
     * deleteButton 이 존재할 경우 클릭 이벤트 리스너를 추가합니다.
     * 클릭 시 해당 글의 ID를 가져와서 DELETE 요청을 보냅니다.
     * 요청이 완료되면 알림을 표시하고 글 목록 페이지로 이동합니다.
     */
    deleteButton.addEventListener('click', event => {
        event.preventDefault();
        let id = document.getElementById('article-id').value;
        if (id) {
            fetch(`/api/articles/${id}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        alert('글이 삭제되었습니다.');
                        location.replace('/articles');
                    } else {
                        alert('글 삭제에 실패했습니다.');
                    }
                }).catch(error => {
                console.error('글 삭제 중 에러 발생:', error);
                alert('글 삭제에 실패했습니다.');
            });
        } else {
            alert('유효한 ID가 아닙니다.');
        }
    })
}

/**
 * 수정 기능을 담당하는 코드입니다.
 * modifyButton 요소를 찾아 클릭 이벤트 리스너를 추가합니다.
 * 수정 시 제목과 내용을 가져와서 PUT 요청을 보냅니다.
 * 요청이 완료되면 알림을 표시하고 해당 글 페이지로 이동합니다.
 * BlogApiController의 @PutMapping("/api/articles/{id}") 로 전송
 */
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    /**
     * 클릭 이벤트가 감지되면 수정 API 요청
     */
    modifyButton.addEventListener('click', event => {
        let param = new URLSearchParams(location.search);
        let id = param.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}

/**
 * 등록 기능을 담당하는 코드입니다.
 * BlogApiController의 @PostMapping("/api/articles") 로 전송
 */
const createButton = document.getElementById('create-btn');

if (createButton) {
    // 클릭 이벤트가 감지되면 등록 API 요청
    createButton.addEventListener('click', (event) => {
        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
            }),
        }).then(response => {
            if (response.ok) {
                alert('등록이 완료되었습니다.');
                location.replace('/articles');
            } else {
                alert('등록에 실패했습니다.');
            }
        }).catch(error => {
            console.error('등록 중 에러 발생:', error);
            alert('등록에 실패했습니다.');
        });
    });
}