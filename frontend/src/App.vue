<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-form :inline="true" :model="query" @submit.prevent>
          <el-form-item label="名称">
            <el-input v-model="query.name" placeholder="请输入名称" clearable style="width: 220px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="query.status" placeholder="请选择" clearable style="width: 160px">
              <el-option :value="1" label="启用" />
              <el-option :value="0" label="禁用" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">搜索</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="openAdd">新增</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="编号" width="80" />
        <el-table-column prop="name" label="名称" min-width="180" />
        <el-table-column prop="orderNo" label="排序号" width="100" />
        <el-table-column prop="unit" label="单位" width="120" />
        <el-table-column prop="price" label="价格" width="120" />
        <el-table-column prop="image" label="图片" width="120">
          <template #default="{ row }">
            <el-image v-if="row.image" :src="row.image" fit="cover" style="width: 50px; height: 50px" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="nursingRequirement" label="护理要求" min-width="220" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">修改</el-button>
            <el-popconfirm title="确认删除？" @confirm="onDelete(row)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination background layout="total, sizes, prev, pager, next, jumper" :total="total" :page-size="page.pageSize"
          :current-page="page.pageNum" :page-sizes="[10, 20, 50, 100]" @update:current-page="(v) => (page.pageNum = v)"
          @update:page-size="(v) => (page.pageSize = v)" @size-change="fetchList" @current-change="fetchList" />
      </div>
    </el-card>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="排序号" prop="orderNo">
          <el-input-number v-model="form.orderNo" :min="0" class="short-number" style="width: 220px" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" class="short-number"
            style="width: 220px" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">禁用</el-radio>
            <el-radio :value="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <div class="image-field">
            <div class="image-preview">
              <el-image :src="form.image" fit="cover" class="image-preview-img"
                :preview-src-list="form.image ? [form.image] : []">
                <template #placeholder>
                  <div class="image-preview-placeholder">+</div>
                </template>
                <template #error>
                  <div class="image-preview-placeholder">+</div>
                </template>
              </el-image>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="护理要求" prop="nursingRequirement">
          <el-input v-model="form.nursingRequirement" type="textarea" :rows="3" placeholder="请输入护理要求" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="dialog.saving" @click="onSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  pageNursingProject,
  addNursingProject,
  updateNursingProject,
  deleteNursingProject
} from './api/nursingProject'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const query = reactive({
  name: '',
  status: undefined
})

const page = reactive({
  pageNum: 1,
  pageSize: 10
})

async function fetchList() {
  loading.value = true
  try {
    const res = await pageNursingProject({
      pageNum: page.pageNum ?? 1,
      pageSize: page.pageSize ?? 10,
      name: query.name || undefined,
      status: query.status
    })
    total.value = Number(res?.total ?? 0)
    tableData.value = Array.isArray(res?.records) ? res.records : []
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function onSearch() {
  page.pageNum = 1
  fetchList()
}

function onReset() {
  query.name = ''
  query.status = undefined
  page.pageNum = 1
  fetchList()
}

const dialog = reactive({
  visible: false,
  title: '新增护理项目',
  saving: false
})

const formRef = ref()
const form = reactive({
  id: null,
  name: '',
  orderNo: null,
  unit: '',
  price: null,
  image: '',
  nursingRequirement: '',
  status: 1,
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

function resetForm() {
  form.id = null
  form.name = ''
  form.orderNo = null
  form.unit = ''
  form.price = null
  form.image = ''
  form.nursingRequirement = ''
  form.status = 1
  form.remark = ''
}

function openAdd() {
  resetForm()
  dialog.title = '新增护理项目'
  dialog.visible = true
}

function openEdit(row) {
  form.id = row.id
  form.name = row.name
  form.orderNo = row.orderNo
  form.unit = row.unit
  form.price = row.price
  form.image = row.image
  form.nursingRequirement = row.nursingRequirement
  form.status = row.status
  form.remark = row.remark
  dialog.title = '修改护理项目'
  dialog.visible = true
}

async function onSave() {
  await formRef.value.validate()
  dialog.saving = true
  try {
    const payload = {
      id: form.id,
      name: form.name,
      orderNo: form.orderNo,
      unit: form.unit,
      price: form.price,
      image: form.image,
      nursingRequirement: form.nursingRequirement,
      status: form.status,
      remark: form.remark
    }
    if (payload.id) {
      await updateNursingProject(payload)
      ElMessage.success('修改成功')
    } else {
      await addNursingProject(payload)
      ElMessage.success('新增成功')
    }
    dialog.visible = false
    fetchList()
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  } finally {
    dialog.saving = false
  }
}

async function onDelete(row) {
  try {
    await deleteNursingProject(row.id)
    ElMessage.success('删除成功')
    if (tableData.value.length === 1 && page.current > 1) {
      page.current -= 1
    }
    fetchList()
  } catch (e) {
    ElMessage.error(e.message || '删除失败')
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.page {
  padding: 16px;
}

.short-number {
  width: 220px;
}

.image-field {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  width: 100%;
}

.image-preview {
  width: 120px;
  height: 120px;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  overflow: hidden;
  flex: 0 0 auto;
}

.image-preview-img {
  width: 100%;
  height: 100%;
}

.image-preview-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: var(--el-text-color-placeholder);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
</style>
