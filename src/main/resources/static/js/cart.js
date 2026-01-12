async function addToCart(productTypeId, quantity = 1) {
    try {
        const response = await fetch('/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `productTypeId=${productTypeId}&quantity=${quantity}`
        });
        
        const data = await response.json();
        
        if (data.success) {
            showToast(data.message, 'success');
            updateCartBadge();
        } else {
            if (data.redirectUrl) {
                
                if (confirm(data.message + '. Chuyển đến trang đăng nhập?')) {
                    window.location.href = data.redirectUrl;
                }
            } else {
                showToast(data.message, 'error');
            }
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        showToast('Có lỗi xảy ra khi thêm vào giỏ hàng', 'error');
    }
}


async function updateCartBadge() {
    try {
        const response = await fetch('/cart/count');
        const data = await response.json();
        const badge = document.getElementById('cartBadge');
        if (badge && data.count > 0) {
            badge.textContent = data.count;
            badge.style.display = 'block';
        } else if (badge) {
            badge.textContent = '';
            badge.style.display = 'none';
        }
    } catch (error) {
        console.error('Error updating cart badge:', error);
    }
}


function showToast(message, type = 'info') {
    
    const existingToast = document.querySelector('.toast-notification');
    if (existingToast) {
        existingToast.remove();
    }
    
    
    const toast = document.createElement('div');
    toast.className = `toast-notification toast-${type}`;
    toast.textContent = message;
    
    
    document.body.appendChild(toast);
    
    
    setTimeout(() => {
        toast.classList.add('show');
    }, 10);
    
    
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);
}

