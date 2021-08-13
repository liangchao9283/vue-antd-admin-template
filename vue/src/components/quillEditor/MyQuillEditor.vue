<template>
    <div>
        <template>
            <div  style="margin-bottom: 50px;">
                <quill-editor
                        v-model="content"
                        ref="myQuillEditor"
                        :options="editorOption"
                        @focus="onEditorFocus($event)"
                        @blur="onEditorBlur($event)"
                        @change="onEditorChange($event)"
                        class="editor"
                ></quill-editor>

                <form action method="post" enctype="multipart/form-data" id="uploadFormMulti">
                    <input
                            style="display: none"
                            :id="uniqueId"
                            type="file"
                            name="file"
                            multiple
                            accept="image/jpg, image/jpeg, image/png, image/gif"
                            @change="uploadImg('uploadFormMulti')"
                    />
                </form>
            </div>
        </template>
    </div>
</template>
<script>
    import "quill/dist/quill.core.css";
    import "quill/dist/quill.snow.css";
    import "quill/dist/quill.bubble.css";

    import { quillEditor } from "vue-quill-editor";
    import { addQuillTitle } from './quill-title.js'

    // 工具栏配置
    const toolbarOptions = [
        ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线 -----['bold', 'italic', 'underline', 'strike']
        ["blockquote", "code-block"], // 引用  代码块-----['blockquote', 'code-block']
        [{ header: 1 }, { header: 2 }], // 1、2 级标题-----[{ header: 1 }, { header: 2 }]
        [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表-----[{ list: 'ordered' }, { list: 'bullet' }]
        [{ script: "sub" }, { script: "super" }], // 上标/下标-----[{ script: 'sub' }, { script: 'super' }]
        [{ indent: "-1" }, { indent: "+1" }], // 缩进-----[{ indent: '-1' }, { indent: '+1' }]
        [{ direction: "rtl" }], // 文本方向-----[{'direction': 'rtl'}]
        [{ size: ["small", false, "large", "huge"] }], // 字体大小-----[{ size: ['small', false, 'large', 'huge'] }]
        [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题-----[{ header: [1, 2, 3, 4, 5, 6, false] }]
        [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色-----[{ color: [] }, { background: [] }]
        [{ font: [] }], // 字体种类-----[{ font: [] }]
        [{ align: [] }], // 对齐方式-----[{ align: [] }]
        ["clean"], // 清除文本格式-----['clean']
        ["image", "video",'link'] // 链接、图片、视频-----['link', 'image', 'video']
    ];

    export default {
        components: {
            quillEditor
        },
        mixins: [],
        computed: {
            //当前富文本实例
            editor() {
                return this.$refs.myQuillEditor.quill;
            }
        },
        props: {
            quillContent: String
        },
        data() {
            return {
                uniqueId: "uniqueId",
                content: this.quillContent, // 富文本编辑器默认内容
                editorOption: {
                    //  富文本编辑器配置
                    modules: {
                        toolbar: toolbarOptions
                    },
                    theme: "snow",
                    placeholder: "请输入正文"
                }
            }
        },
        methods: {
            // 准备富文本编辑器
            onEditorReady(quill) {

            },
            // 富文本编辑器 失去焦点事件
            onEditorBlur(quill) {

            },
            // 富文本编辑器 获得焦点事件
            onEditorFocus(quill) {

            },
            // 富文本编辑器 内容改变事件
            onEditorChange({ quill, html, text }) {
                //this.content = html
                this.$emit('editorChange',html)
            },
            uploadImg: async function() {
                var _this = this;
                //构造formData对象
                var formData = new FormData();
                formData.append("file", document.getElementById(_this.uniqueId).files[0]);

                /*try {
                    //调用上传文件接口
                    this.$http.productapi.uploadImgReq(formData).then(res => {
                        //返回上传文件的地址
                        let url = res;
                        if (url != null && url.length > 0) {
                            let Range = _this.editor.getSelection();
                            url = url.indexOf("http") != -1 ? url : "http:" + url;
                            //上传文件成功之后在富文本中回显(显示)
                            _this.editor.insertEmbed(
                                Range != null ? Range.index : 0,
                                "image",
                                url
                            );
                        } else {
                            _this.$message.warning("图片上传失败");
                        }
                        //成功之后,将文件的文本框的value置空
                        document.getElementById(_this.uniqueId).value = "";
                    });
                } catch ({ message: msg }) {
                    document.getElementById(_this.uniqueId).value = "";
                    _this.$message.warning(msg);
                }*/
            }

        },
        created() {
        },
        mounted() {
            var _this = this;
            var imgHandler = async function(image) {
                if (image) {
                    var fileInput = document.getElementById(_this.uniqueId); //隐藏的file文本ID
                    fileInput.click(); //加一个触发事件
                }
            };
            _this.editor.getModule("toolbar").addHandler("image", imgHandler);
            addQuillTitle();
        }
    }
</script>
<style>
    .editor {
        line-height: normal !important;
        height: 300px;
    }
    .ql-snow .ql-tooltip[data-mode="link"]::before {
        content: "请输入链接地址:";
    }
    .ql-snow .ql-tooltip.ql-editing a.ql-action::after {
        border-right: 0px;
        content: "保存";
        padding-right: 0px;
    }

    .ql-snow .ql-tooltip[data-mode="video"]::before {
        content: "请输入视频地址:";
    }

    .ql-snow .ql-picker.ql-size .ql-picker-label::before,
    .ql-snow .ql-picker.ql-size .ql-picker-item::before {
        content: "14px";
    }
    .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
    .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
        content: "10px";
    }
    .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
    .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
        content: "18px";
    }
    .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
    .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
        content: "32px";
    }

    .ql-snow .ql-picker.ql-header .ql-picker-label::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item::before {
        content: "文本";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
        content: "标题1";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
        content: "标题2";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
        content: "标题3";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
        content: "标题4";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
        content: "标题5";
    }
    .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
    .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
        content: "标题6";
    }

    .ql-snow .ql-picker.ql-font .ql-picker-label::before,
    .ql-snow .ql-picker.ql-font .ql-picker-item::before {
        content: "标准字体";
    }
    .ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
    .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
        content: "衬线字体";
    }
    .ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
    .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
        content: "等宽字体";
    }
</style>

