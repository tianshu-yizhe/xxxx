document.addEventListener('DOMContentLoaded', function() {
    const roleSelect = document.getElementById('role');
    const residentSelect = document.getElementById('residentSelect');

    if (roleSelect && residentSelect) {
        roleSelect.addEventListener('change', function() {
            if (this.value === 'RESIDENT') {
                residentSelect.style.display = 'block';
            } else {
                residentSelect.style.display = 'none';
            }
        });

        // 初始化状态
        if (roleSelect.value !== 'RESIDENT') {
            residentSelect.style.display = 'none';
        }
    }

    // 表单验证
    const form = document.getElementById('registerForm');
    if (form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    }
});