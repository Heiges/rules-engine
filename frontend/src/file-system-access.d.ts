interface SaveFilePickerOptions {
  suggestedName?: string
  types?: Array<{
    description?: string
    accept: Record<string, string[]>
  }>
  excludeAcceptAllOption?: boolean
}

interface OpenFilePickerOptions {
  types?: Array<{
    description?: string
    accept: Record<string, string[]>
  }>
  excludeAcceptAllOption?: boolean
  multiple?: boolean
}

interface Window {
  showSaveFilePicker?(options?: SaveFilePickerOptions): Promise<FileSystemFileHandle>
  showOpenFilePicker?(options?: OpenFilePickerOptions): Promise<FileSystemFileHandle[]>
}
