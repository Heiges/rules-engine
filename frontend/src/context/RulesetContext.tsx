import { createContext, useContext, useState } from 'react'
import type { ReactNode } from 'react'
import type { RulesetData } from '../api'

interface RulesetContextType {
  currentRuleset: string | null
  setCurrentRuleset: (name: string | null) => void
  rulesetData: RulesetData | null
  setRulesetData: (data: RulesetData | null) => void
  fileHandle: FileSystemFileHandle | null
  setFileHandle: (handle: FileSystemFileHandle | null) => void
}

const RulesetContext = createContext<RulesetContextType | null>(null)

export function RulesetProvider({ children }: { children: ReactNode }) {
  const [currentRuleset, setCurrentRuleset] = useState<string | null>(null)
  const [rulesetData, setRulesetData] = useState<RulesetData | null>(null)
  const [fileHandle, setFileHandle] = useState<FileSystemFileHandle | null>(null)

  return (
    <RulesetContext.Provider value={{ currentRuleset, setCurrentRuleset, rulesetData, setRulesetData, fileHandle, setFileHandle }}>
      {children}
    </RulesetContext.Provider>
  )
}

export function useRuleset(): RulesetContextType {
  const ctx = useContext(RulesetContext)
  if (!ctx) throw new Error('useRuleset must be used within RulesetProvider')
  return ctx
}
