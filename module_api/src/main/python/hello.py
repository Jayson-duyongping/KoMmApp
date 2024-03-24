from PIL import Image
from io import BytesIO

def getResizeBitmap(img_bytes, size1, size2):
    # 将接收到的字节数组转换为图像
    img = Image.open(BytesIO(img_bytes)).convert('RGB')
    # 调整图像大小
    resized_img = img.resize((size1, size2), Image.BILINEAR)
    # 将调整大小后的图像保存到字节流
    output_buffer = BytesIO()
    resized_img.save(output_buffer, format='PNG')
    byte_data = output_buffer.getvalue()
    # 返回处理后的图像字节数组
    return byte_data


def sayHello():
    return "Hello World"
